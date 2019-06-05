package de.leonlatsch.oliviabackend.dto;

public class UserDTO {

    private int uid;
    private String username;
    private String email;
    private String profilePic;

    public UserDTO(int uid, String username, String email, String profilePic) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.profilePic = profilePic;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
