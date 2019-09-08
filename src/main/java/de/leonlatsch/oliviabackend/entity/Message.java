package de.leonlatsch.oliviabackend.entity;

import de.leonlatsch.oliviabackend.constants.MessageType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Blob;
import java.sql.Timestamp;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @Column(name = "mid", length = 36)
    private String mid;

    @Column(name = "uid_from")
    private int from;

    @Column(name = "uid_to")
    private int to;

    @Column(name = "content")
    private Blob content;

    @Column(name = "type")
    private MessageType type;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "cid")
    private String cid;

    public Message() {}

    public Message(String mid, int from, int to, Blob content, int type, Timestamp timestamp, String cid) {
        this.mid = mid;
        this.from = from;
        this.to = to;
        this.content = content;
        this.type = MessageType.valueOf(type);
        this.timestamp = timestamp;
        this.cid = cid;
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

    public Blob getContent() {
        return content;
    }

    public void setContent(Blob content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(int type) {
        this.type = MessageType.valueOf(type);
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
