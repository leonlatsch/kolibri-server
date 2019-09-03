package de.leonlatsch.oliviabackend.dto;

public enum StdResponse {

    OK("OK"),
    ERROR("ERROR"),
    TAKEN("TAKEN"),
    FREE("FREE"),
    FAIL("FAIL"),
    AUTORIZED("AUTORIZED"),
    UNAUTORIZED("UNAUTORIZED");

    private String message;

    private StdResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
