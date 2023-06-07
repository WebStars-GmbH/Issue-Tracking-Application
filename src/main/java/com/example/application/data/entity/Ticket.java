package com.example.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

@Entity
public class Ticket extends AbstractEntity{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "website_id")
    @NotNull
    @JsonIgnoreProperties({"tickets"})
    private Website website;
    //@NotBlank
    private Timestamp register_date;
    //@NotBlank

    private String status;

    @NotBlank
    private String header;

    private Timestamp assign_date;

    private Timestamp close_date;
    //@NotBlank
    private Timestamp last_update;

    private String registered_by;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tuser_id")
    //@NotNull
    @JsonIgnoreProperties({"assigned_tickets"})
    private TUser assigned_to;

    /*
    @ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "tuser_id")
    private TUser closed_by;
    */

    @NotBlank
    private String description = "";

    private String solution = "";

    private int priority = 0;
    //@NotBlank
    private String history = "";

    //Constructors
    public Ticket(){}

    //Getters and Setters
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

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
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

    public String getRegistered_by() {
        return registered_by;
    }

    public void setRegistered_by(String registered_by) {
        this.registered_by = registered_by;
    }

    public TUser getAssigned_to() {
        return assigned_to;
    }

    public void setAssigned_to(TUser assigned_to) {
        this.assigned_to = assigned_to;
    }

    /*
    public TUser getClosed_by() {
        return closed_by;
    }

    public void setClosed_by(TUser closed_by) {
        this.closed_by = closed_by;
    }
    */

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
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

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}

