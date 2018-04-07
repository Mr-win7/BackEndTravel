package net.hustonline.account.domain;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.Date;
import java.util.List;

public class AccountBook {

    @JsonView(View.Summary.class)
    private Integer id;

    @JsonView(View.Summary.class)
    private String destination;

    @JsonView(View.Summary.class)
    private Date time;

    @JsonView(View.AccountBook.class)
    private List<PublicBill> publicBills;

    @JsonView(View.AccountBook.class)
    private List<PrivateBill> privateBills;

    @JsonView(View.AccountBook.class)
    private List<User> participants;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination == null ? null : destination.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<PublicBill> getPublicBills() {
        return publicBills;
    }

    public void setPublicBills(List<PublicBill> publicBills) {
        this.publicBills = publicBills;
    }

    public List<PrivateBill> getPrivateBills() {
        return privateBills;
    }

    public void setPrivateBills(List<PrivateBill> privateBills) {
        this.privateBills = privateBills;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }
}