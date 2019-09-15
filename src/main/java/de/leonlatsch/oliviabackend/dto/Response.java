package de.leonlatsch.oliviabackend.dto;

import de.leonlatsch.oliviabackend.constants.Formats;

import java.sql.Timestamp;

public class Response {

    private int code;
    private String message;
    private String timestamp;
    private Object content;

    public Response() {}

    public Response(int code, String message, Object content) {
        this.code = code;
        this.message = message;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.timestamp = Formats.DATE_FORMAT.format(timestamp);
    }
}