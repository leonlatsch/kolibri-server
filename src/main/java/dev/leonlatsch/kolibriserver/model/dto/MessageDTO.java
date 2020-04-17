package dev.leonlatsch.kolibriserver.model.dto;

import dev.leonlatsch.kolibriserver.constants.FormatsAndFiles;
import dev.leonlatsch.kolibriserver.model.MessageType;
import dev.leonlatsch.kolibriserver.model.ValidatedModel;

import java.text.ParseException;

/**
 * DTO containing a chat message.
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
public class MessageDTO extends ValidatedModel {

    private String mid;
    private String from;
    private String to;
    private String content;
    private MessageType type;
    private String timestamp;

    public MessageDTO() {
    }

    public MessageDTO(String mid, String from, String to, String content, MessageType type, String timestamp) {
        this.mid = mid;
        this.from = from;
        this.to = to;
        this.content = content;
        this.type = type;
        this.timestamp = timestamp;
    }

    @Override
    public boolean validate() {
        boolean isTimestampValid;
        try {
            FormatsAndFiles.DATE_FORMAT.parse(timestamp);
            isTimestampValid = true;
        } catch (ParseException e) {
            isTimestampValid = false;
        }
        return mid != null && from != null && to != null && content != null && type != null && type != MessageType.UNDEFINED && timestamp != null && isTimestampValid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
