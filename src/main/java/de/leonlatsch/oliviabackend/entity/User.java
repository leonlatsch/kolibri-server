package de.leonlatsch.oliviabackend.entity;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "uid")
    private int uid;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "passwordHash")
    private String passwordHash;

    @Lob
    @Column(name = "profilePic")
    private Blob profilePic;

    public User(int uid, String username, String email, String passwordHash, Blob profilePic) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.profilePic = profilePic;
    }

    public User() {}

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Blob getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Blob profilePic) {
        this.profilePic = profilePic;
    }
}
