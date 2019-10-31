package de.leonlatsch.oliviabackend.rest;

import de.leonlatsch.oliviabackend.dto.rabbitmq.RMQPermissions;
import de.leonlatsch.oliviabackend.dto.rabbitmq.RMQUser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RabbitMQRestService {

    String USER_ENDPOINT = "api/users/{name}";
    String PERMISSIONS_ENDPOINT = "api/permissions/%2F/{name}";

    @PUT(USER_ENDPOINT)
    Call createUser(@Path("name") String name, @Body RMQUser user);

    @DELETE(USER_ENDPOINT)
    Call deleteUser(@Path("name") String name);

    @PUT(PERMISSIONS_ENDPOINT)
    Call setPermissions(@Path("user") String user, @Body RMQPermissions permissions);
}
