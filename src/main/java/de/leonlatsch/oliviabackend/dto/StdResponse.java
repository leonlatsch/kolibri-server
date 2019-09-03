package de.leonlatsch.oliviabackend.dto;

public class StdResponse {

    private String message;

    public StdResponse() {}

    public StdResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
