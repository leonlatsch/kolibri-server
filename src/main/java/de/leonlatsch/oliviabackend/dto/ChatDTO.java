package de.leonlatsch.oliviabackend.dto;

public class ChatDTO {

    private String cid;
    private int first_member;
    private int secondMember;

    public ChatDTO(String cid, int first_member, int secondMember) {
        this.cid = cid;
        this.first_member = first_member;
        this.secondMember = secondMember;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getFirst_member() {
        return first_member;
    }

    public void setFirst_member(int first_member) {
        this.first_member = first_member;
    }

    public int getSecondMember() {
        return secondMember;
    }

    public void setSecondMember(int secondMember) {
        this.secondMember = secondMember;
    }
}
