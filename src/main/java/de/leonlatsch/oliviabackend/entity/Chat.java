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
    private int firstMember;

    @Column(name = "second_member")
    private int secondMember;

    public Chat(String cid, int firstMember, int secondMember) {
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

    public int getFirstMember() {
        return firstMember;
    }

    public void setFirstMember(int firstMember) {
        this.firstMember = firstMember;
    }

    public int getSecondMember() {
        return secondMember;
    }

    public void setSecondMember(int secondMember) {
        this.secondMember = secondMember;
    }
}
