package de.leonlatsch.oliviabackend.dto;

public class AuthResponse {

    private String message;
    private String accessToken;
    private boolean success;

    public AuthResponse() {}

    public AuthResponse(String message, String accessToken, boolean success) {
        this.message = message;
        this.accessToken = accessToken;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
