package dev.leonlatsch.oliviabackend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DB model for saving access tokens of a user.
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
@Entity
@Table(name = "access_token")
public class AccessToken {

    @Id
    @Column(name = "token", length = 24)
    private String token;

    @Column(name = "uid")
    private String uid;

    @Column(name = "valid")
    private boolean valid;

    public AccessToken() {
    }

    public AccessToken(String token, String uid, boolean valid) {
        this.token = token;
        this.uid = uid;
        this.valid = valid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
