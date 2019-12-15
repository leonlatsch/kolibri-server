package dev.leonlatsch.oliviabackend.dto;

/**
 * DTO containing a chat message.
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
public class MessageDTO {

    private String mid;
    private String from;
    private String to;
    private String content;
    private String type;
    private String timestamp;
    private String cid;

    public MessageDTO() {
    }

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
