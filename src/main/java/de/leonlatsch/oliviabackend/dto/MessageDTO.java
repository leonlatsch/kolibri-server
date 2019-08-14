package de.leonlatsch.oliviabackend.dto;

import de.leonlatsch.oliviabackend.constants.MessageType;

public class MessageDTO {

    private String mid; // The unique message id
    private int from; // user's uid that sent the message
    private int to; // user's uid that will receive the message
    private String content; // base64 encoded binary content
    private MessageType type; // The type of the message, used for mapping the content
    private String timestamp; // The timestamp the message was sent

    public MessageDTO() {}

    public MessageDTO(String mid, int from, int to, String content, MessageType type, String timestamp) {
        this.mid = mid;
        this.from = from;
        this.to = to;
        this.content = content;
        this.type = type;
        this.timestamp = timestamp;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
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
