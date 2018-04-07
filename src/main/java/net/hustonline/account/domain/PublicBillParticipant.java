package net.hustonline.account.domain;

public class PublicBillParticipant {
    private Integer id;

    private Integer publicBillId;

    private Integer userId;

    public Integer getId() {
        return id;
    }

    public PublicBillParticipant setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getPublicBillId() {
        return publicBillId;
    }

    public PublicBillParticipant setPublicBillId(Integer publicBillId) {
        this.publicBillId = publicBillId;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public PublicBillParticipant setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }
}