package dev.leonlatsch.kolibriserver.model.dto;

import dev.leonlatsch.kolibriserver.constants.FormatsAndFiles;

import java.sql.Timestamp;

/**
 * DTO containing general information about a rest response and a variable return object.</br>
 * Used for every request.
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
public class Container {

    private int code;
    private String message;
    private String timestamp;
    private Object content;

    public Container() {
    }

    public Container(int code, String message, Object content) {
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
        this.timestamp = FormatsAndFiles.DATE_FORMAT.format(timestamp);
    }
}
