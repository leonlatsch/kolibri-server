package de.leonlatsch.oliviabackend.dto;

public class ChatDTO {

    private String cid;
    private String first_member;
    private String secondMember;

    public ChatDTO() {}

    public ChatDTO(String cid, String first_member, String secondMember) {
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

    public String getFirst_member() {
        return first_member;
    }

    public void setFirst_member(String first_member) {
        this.first_member = first_member;
    }

    public String getSecondMember() {
        return secondMember;
    }

    public void setSecondMember(String secondMember) {
        this.secondMember = secondMember;
    }
}
