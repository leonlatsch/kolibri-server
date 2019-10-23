package de.leonlatsch.oliviabackend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "chat")
public class Chat {

    @Id
    @Column(name = "cid", length = 36)
    private String cid;

    @Column(name = "first_member")
    private String firstMember;

    @Column(name = "second_member")
    private String secondMember;

    public Chat() {}

    public Chat(String cid, String firstMember, String secondMember) {
        this.cid = cid;
        this.firstMember = firstMember;
        this.secondMember = secondMember;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getFirstMember() {
        return firstMember;
    }

    public void setFirstMember(String firstMember) {
        this.firstMember = firstMember;
    }

    public String getSecondMember() {
        return secondMember;
    }

    public void setSecondMember(String secondMember) {
        this.secondMember = secondMember;
    }
}
