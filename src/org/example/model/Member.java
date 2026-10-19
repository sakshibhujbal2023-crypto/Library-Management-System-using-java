package org.example.model;

public class Member {
    private int memberId;
    private String memberName;
    private String email;
    private String mobile;
    private String address;

    public Member() {}

    public Member(int memberId, String memberName, String email, String mobile, String address) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
    }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return String.format("%-6d %-24s %-30s %-16s %s",
                memberId, memberName, email == null ? "" : email,
                mobile == null ? "" : mobile, address == null ? "" : address);
    }
}
