package dev.leonlatsch.oliviabackend.rest;

import dev.leonlatsch.oliviabackend.dto.rabbitmq.RMQPermissions;
import dev.leonlatsch.oliviabackend.dto.rabbitmq.RMQUser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * @author Leon Latsch
 * @since 1.0.0
 */
public interface RabbitMQRestService {

    String USER_ENDPOINT = "api/users/{name}";
    String PERMISSIONS_ENDPOINT = "api/permissions/%2F/{name}";

    @PUT(USER_ENDPOINT)
    Call<Void> createUser(@Path("name") String name, @Body RMQUser user);

    @DELETE(USER_ENDPOINT)
    Call<Void> deleteUser(@Path("name") String name);

    @PUT(PERMISSIONS_ENDPOINT)
    Call<Void> setPermissions(@Path("name") String user, @Body RMQPermissions permissions);
}
