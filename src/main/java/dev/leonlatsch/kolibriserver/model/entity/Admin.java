package dev.leonlatsch.kolibriserver.model.entity;

import dev.leonlatsch.kolibriserver.model.ValidatedModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DB model to save a admin model
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
@Entity
@Table(name = "admin")
public class Admin extends ValidatedModel {

    @Id
    @Column(name = "username", length = 36)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "token")
    private String token;

    public Admin() {
    }

    public Admin(String username, String password, String token) {
        this.username = username;
        this.password = password;
        this.token = token;
    }

    @Override
    public boolean validate() {
        return username != null && password != null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
