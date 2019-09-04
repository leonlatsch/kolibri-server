package de.leonlatsch.oliviabackend.dto;

public class PublicUserDTO {

    private int uid;
    private String username;
    private String profilePicTn;

    public PublicUserDTO() {}

    public PublicUserDTO(int uid, String username, String profilePicTn) {
        this.uid = uid;
        this.username = username;
        this.profilePicTn = profilePicTn;
    }

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

    public String getProfilePicTn() {
        return profilePicTn;
    }

    public void setProfilePicTn(String profilePicTn) {
        this.profilePicTn = profilePicTn;
    }
}
