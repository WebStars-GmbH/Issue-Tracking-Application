package com.example.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Entity(name="entity_ticket")
@Table(name="Ticket")
public class Ticket {
    @Id
    @Column(name = "ticket_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticket_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "website_id")
    private Website website;
    @NotBlank
    private Timestamp register_date;
    @NotBlank
    private String status;

    private Timestamp assign_date;

    private Timestamp close_date;
    @NotBlank
    private Timestamp last_update;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User registered_by;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User assigned_to;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User closed_by;
    @NotBlank
    private String description_text;

    private String resolution_text;
    @NotBlank
    private int priority;
    @NotBlank
    private String history;

    //Constructors
    public Ticket(){}

    public Ticket(User user, Website website, String description_text){
        this.registered_by = user;
        this.website = website;

        this.description_text = description_text;
        this.status = "Registered";

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.register_date = timestamp;
        this.last_update = timestamp;
        String timestampString = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(timestamp);
        this.history = timestampString + ": Ticket created by " + user.toString();
    }


    //Getters and Setters
    public Long getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(Long ticket_id) {
        this.ticket_id = ticket_id;
    }

    public Timestamp getRegister_date() {
        return register_date;
    }

    public void setRegister_date(Timestamp register_date) {
        this.register_date = register_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getAssign_date() {
        return assign_date;
    }

    public void setAssign_date(Timestamp assign_date) {
        this.assign_date = assign_date;
    }

    public Timestamp getClose_date() {
        return close_date;
    }

    public void setClose_date(Timestamp close_date) {
        this.close_date = close_date;
    }

    public Timestamp getLast_update() {
        return last_update;
    }

    public void setLast_update(Timestamp last_update) {
        this.last_update = last_update;
    }

    public User getRegistered_by() {
        return registered_by;
    }

    public void setRegistered_by(User registered_by) {
        this.registered_by = registered_by;
    }

    public User getAssigned_to() {
        return assigned_to;
    }

    public void setAssigned_to(User assigned_to) {
        this.assigned_to = assigned_to;
    }

    public User getClosed_by() {
        return closed_by;
    }

    public void setClosed_by(User closed_by) {
        this.closed_by = closed_by;
    }

    public String getDescription_text() {
        return description_text;
    }

    public void setDescription_text(String description_text) {
        this.description_text = description_text;
    }

    public String getResolution_text() {
        return resolution_text;
    }

    public void setResolution_text(String resolution_text) {
        this.resolution_text = resolution_text;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
