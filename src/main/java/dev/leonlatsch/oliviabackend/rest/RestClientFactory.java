package dev.leonlatsch.oliviabackend.rest;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

@Component
public class RestClientFactory {

    private Retrofit retrofit;

    private RabbitMQRestService rabbitMQRestService;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.http-port}")
    private String httpPort;

    @Value("${spring.rabbitmq.username}")
    private String user;

    @Value("${spring.rabbitmq.password}")
    private String password;

    private String buildBaseUrl() {
        return "http://" + host + ":" + httpPort + "";
    }

    private void createRetrofit() {
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new AuthInterceptor(user, password));

            retrofit = new Retrofit.Builder()
                    .baseUrl(buildBaseUrl())
                    .client(httpClient.build())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }
    }

    public RabbitMQRestService getRabbitMQRestService() {
        createRetrofit();

        if (rabbitMQRestService == null) {
            rabbitMQRestService = retrofit.create(RabbitMQRestService.class);
        }

        return rabbitMQRestService;
    }

    private static class AuthInterceptor implements Interceptor {

        private String user;
        private String password;

        AuthInterceptor(String user, String password) {
            this.user = user;
            this.password = password;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request orig = chain.request();
            Request request = orig.newBuilder()
                    .addHeader("Authorization", Credentials.basic(user, password))
                    .method(orig.method(), orig.body())
                    .build();

            return chain.proceed(request);
        }
    }
}
