package dev.leonlatsch.oliviabackend.model.dto;

import dev.leonlatsch.oliviabackend.model.ValidatedModel;

/**
 * DTO containing a user without the sensitive information.</br>
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
public class PublicUserDTO extends ValidatedModel {

    private String uid;
    private String username;
    private String profilePicTn;

    public PublicUserDTO() {
    }

    public PublicUserDTO(String uid, String username, String profilePicTn) {
        this.uid = uid;
        this.username = username;
        this.profilePicTn = profilePicTn;
    }

    @Override
    public boolean validate() {
        return uid != null && username != null;
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

    public String getProfilePicTn() {
        return profilePicTn;
    }

    public void setProfilePicTn(String profilePicTn) {
        this.profilePicTn = profilePicTn;
    }
}
