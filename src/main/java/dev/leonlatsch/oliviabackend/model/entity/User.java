package dev.leonlatsch.oliviabackend.model.entity;

import dev.leonlatsch.oliviabackend.model.ValidatedModel;

import javax.persistence.*;
import java.sql.Blob;

/**
 * DB model for saving a user
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
@Entity
@Table(name = "user")
public class User extends ValidatedModel {

    @Id
    @Column(name = "uid", length = 36)
    private String uid;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Lob
    @Column(name = "profile_pic")
    private Blob profilePic;

    @Lob
    @Column(name = "profile_pic_tn")
    private Blob profilePicTn;

    @Lob
    @Column(name = "public_key")
    private Blob publicKey;

    public User() {
    }

    public User(String uid, String username, String email, String password, Blob profilePic, Blob profilePicTn, Blob publicKey) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profilePic = profilePic;
        this.profilePicTn = profilePicTn;
        this.publicKey = publicKey;
    }

    @Override
    public boolean validate() {
        return uid != null && username != null && email != null && password != null && publicKey != null;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Blob getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Blob profilePic) {
        this.profilePic = profilePic;
    }

    public Blob getProfilePicTn() {
        return profilePicTn;
    }

    public void setProfilePicTn(Blob profilePicTn) {
        this.profilePicTn = profilePicTn;
    }

    public Blob getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(Blob publicKey) {
        this.publicKey = publicKey;
    }
}
