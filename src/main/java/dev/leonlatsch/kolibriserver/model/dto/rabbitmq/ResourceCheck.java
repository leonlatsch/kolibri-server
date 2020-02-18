package dev.leonlatsch.kolibriserver.model.dto.rabbitmq;

/**
 * Transfer object used by rabbitmq to define a requested resource
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
public class ResourceCheck {

    private String username;

    private String vhost;
    private String resource;
    private String name;
    private String permission;

    public ResourceCheck() {
    }

    public ResourceCheck(String username, String vhost, String resource, String name, String permission) {
        this.username = username;
        this.vhost = vhost;
        this.resource = resource;
        this.name = name;
        this.permission = permission;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}