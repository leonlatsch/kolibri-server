package dev.leonlatsch.oliviabackend.rest;

import dev.leonlatsch.oliviabackend.model.dto.rabbitmq.RMQPermissions;
import dev.leonlatsch.oliviabackend.model.dto.rabbitmq.RMQUser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Rest service to communicate with RabbitMQ's http api
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
public interface RabbitMQRestService {

    String USER_ENDPOINT = "api/users/{name}";
    String PERMISSIONS_ENDPOINT = "api/permissions/%2F/{name}";

    /**
     * Create a RabbitMQ user in the queue instance.
     *
     * @param name Username. Sould be user.{uid}
     * @param user {@link RMQUser} with the password
     * @return void
     */
    @PUT(USER_ENDPOINT)
    Call<Void> createUser(@Path("name") String name, @Body RMQUser user);

    /**
     * Delete a RAmmitMQ user in the queue instance.
     *
     * @param name the username to delete
     * @return void
     */
    @DELETE(USER_ENDPOINT)
    Call<Void> deleteUser(@Path("name") String name);

    /**
     * Set the permissions of a RammitMQ user.
     *
     * @param user        The username of the user.
     * @param permissions {@link RMQPermissions} with the permissions to set
     * @return void
     */
    @PUT(PERMISSIONS_ENDPOINT)
    Call<Void> setPermissions(@Path("name") String user, @Body RMQPermissions permissions);
}
