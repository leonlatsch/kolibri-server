package dev.leonlatsch.oliviabackend.model.dto;

import dev.leonlatsch.oliviabackend.model.ValidatedModel;

/**
 * DTO containing a user with all it's information
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
public class UserDTO extends ValidatedModel {

    private String uid;
    private String username;
    private String email;
    private String password;
    private String profilePic;
    private String profilePicTn;

    public UserDTO() {
    }

    public UserDTO(String uid, String username, String email, String password, String profilePic, String profilePicTn) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profilePic = profilePic;
        this.profilePicTn = profilePicTn;
    }

    @Override
    public boolean validate() {
        return uid != null && username != null && email != null && password != null;
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getProfilePicTn() {
        return profilePicTn;
    }

    public void setProfilePicTn(String profilePicTn) {
        this.profilePicTn = profilePicTn;
    }
}
