package de.leonlatsch.oliviabackend.dto;

public class MessageDTO {

    private String mid; // The unique message id
    private String from; // user's uid that sent the message
    private String to; // user's uid that will receive the message
    private String content; // base64 encoded binary content
    private String type; // The type of the message, used for mapping the content
    private String timestamp; // The timestamp the message was sent
    private String cid; // Chat FK to the chat

    public MessageDTO() {}

    public MessageDTO(String mid, String from, String to, String content, String type, String timestamp, String cid) {
        this.mid = mid;
        this.from = from;
        this.to = to;
        this.content = content;
        this.type = type;
        this.timestamp = timestamp;
        this.cid = cid;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
