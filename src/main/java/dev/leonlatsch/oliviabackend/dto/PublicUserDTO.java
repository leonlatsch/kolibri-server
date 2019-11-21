package dev.leonlatsch.oliviabackend.dto;

public class PublicUserDTO {

    private String uid;
    private String username;
    private String profilePicTn;

    public PublicUserDTO() {}

    public PublicUserDTO(String uid, String username, String profilePicTn) {
        this.uid = uid;
        this.username = username;
        this.profilePicTn = profilePicTn;
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
