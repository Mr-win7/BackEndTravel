package net.hustonline.account.domain;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.Date;
import java.util.List;

public class PublicBill {

    @JsonView(View.AccountBook.class)
    private Integer id;

    @JsonView(View.AccountBook.class)
    private String category;

    @JsonView(View.AccountBook.class)
    private Integer sum;

    @JsonView(View.AccountBook.class)
    private Date time;

    @JsonView(View.AccountBook.class)
    private Boolean isSettled;

    @JsonView(View.PublicBill.class)
    private String note;

    @JsonView(View.PublicBill.class)
    private String image;

    @JsonView(View.AccountBook.class)
    private String summary;

    @JsonView(View.PublicBill.class)
    private Integer accountBookId;

    @JsonView(View.AccountBook.class)
    private List<User> participants;

    @JsonView(View.AccountBook.class)
    private Integer payerId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Boolean getIsSettled() {
        return isSettled;
    }

    public void setIsSettled(Boolean isSettled) {
        this.isSettled = isSettled;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public Integer getAccountBookId() {
        return accountBookId;
    }

    public void setAccountBookId(Integer accountBookId) {
        this.accountBookId = accountBookId;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public Integer getPayerId() {
        return payerId;
    }

    public void setPayerId(Integer payerId) {
        this.payerId = payerId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}