package de.leonlatsch.oliviabackend.rest;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import retrofit2.Retrofit;

import java.io.IOException;

public class RestClientFactory {

    private static Retrofit retrofit;

    private static RabbitMQRestService rabbitMQRestService;

    @Value("${spring.rabbitmq.host}")
    private static String host;

    @Value("${spring.rabbitmq.http-port}")
    private static String httpPort;

    @Value("${spring.rabbitmq.user}")
    private static String user;

    @Value("${spring.rabbitmq.password}")
    private static String password;

    private static String buildBaseUrl() {
        return "http://" + host + ":" + httpPort + "";
    }

    private static void createRetrofit() {
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new AuthInterceptor(user, password));

            retrofit = new Retrofit.Builder()
                    .baseUrl(buildBaseUrl())
                    .client(httpClient.build())
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
                    .addHeader("Authorization", "Basic " + Credentials.basic(user, password))
                    .method(orig.method(), orig.body())
                    .build();

            return chain.proceed(request);
        }
    }
}
