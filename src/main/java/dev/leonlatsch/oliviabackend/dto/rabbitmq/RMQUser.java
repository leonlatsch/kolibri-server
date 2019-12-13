package dev.leonlatsch.oliviabackend.dto.rabbitmq;

/**
 * @author Leon Latsch
 * @since 1.0.0
 */
public class RMQUser {

    private String password;

    private String tags;

    public RMQUser() {
    }

    public RMQUser(String password, String tags) {
        this.password = password;
        this.tags = tags;
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
