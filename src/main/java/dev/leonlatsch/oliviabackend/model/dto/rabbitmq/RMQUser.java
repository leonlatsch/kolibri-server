package dev.leonlatsch.oliviabackend.model.dto.rabbitmq;

import dev.leonlatsch.oliviabackend.model.ValidatedModel;

/**
 * DTO containing a RabbitMQ user
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
public class RMQUser extends ValidatedModel {

    private String password;

    private String tags;

    public RMQUser() {
    }

    public RMQUser(String password, String tags) {
        this.password = password;
        this.tags = tags;
    }

    @Override
    public boolean validate() {
        return password != null && tags != null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
