package com.example.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
public class Ticket extends AbstractEntity{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "website_id")
    @NotNull
    @JsonIgnoreProperties({"tickets"})
    private Website website;

    private Timestamp register_date;

    private String status = "";

    @NotBlank
    private String header = "";

    private Timestamp assign_date;

    private Timestamp close_date;
    private Timestamp last_update;

    private String registered_by;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tuser_id")
    //@NotNull
    @JsonIgnoreProperties({"assigned_tickets"})
    private TUser assigned_to;

    private String closed_by;

    @NotBlank
    @Column(columnDefinition = "nvarchar(MAX)")
    private String description = "";

    @Column(columnDefinition = "nvarchar(MAX)")
    private String solution = "";

    private int priority = 0;

    @Column(nullable = true, columnDefinition = "nvarchar(MAX)")
    private String history = "";

    //Constructors
    public Ticket(){}

    //Getters and Setters
    public LocalDate getRegister_date() {
        return register_date.toLocalDateTime().toLocalDate();
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

    public LocalDate getAssign_date() {
        return assign_date.toLocalDateTime().toLocalDate();
    }

    public void setAssign_date(Timestamp assign_date) {
        this.assign_date = assign_date;
    }

    public LocalDate getClose_date() {
        if (close_date == null) {
            return null;
        }
        return close_date.toLocalDateTime().toLocalDate();
    }

    public void setClose_date(Timestamp close_date) {
        this.close_date = close_date;
    }

    public LocalDate getLast_update() {
        return last_update.toLocalDateTime().toLocalDate();
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


    public String getClosed_by() {
        return closed_by;
    }

    public void setClosed_by(String closed_by) {
        this.closed_by = closed_by;
    }

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

    public String getWebsite_name(){
        return this.website.getWebsite_name();
    }
    public String getAssigned_to_username(){
        if (this.assigned_to == null) return "";
        else return this.assigned_to.getUsername();
    }

    public String getRegisterDateString(){
        return "" + this.register_date;
    }
    public String getAssignedDateString(){
        return "" + this.assign_date;
    }
    public String getLastUpdateString(){
        return "" + this.last_update;
    }
    public String getClosedDateString(){
        return "" + this.close_date;
    }

    public long getTimeBetweenAssignedAndSolved(){
        if (assign_date == null || close_date == null) return 0;
        return (this.close_date.getTime() - this.assign_date.getTime());
    }

    public long getTimeBetweenRegisteredAndSolved(){
        if (close_date == null) return 0;
        return (this.close_date.getTime() - this.register_date.getTime());
    }

    public long getTimeBetweenRegisteredAndCancelled(){
        if (!this.getStatus().equals("Cancelled")) return 0;
        return (this.last_update.getTime() - this.register_date.getTime());
    }

}

