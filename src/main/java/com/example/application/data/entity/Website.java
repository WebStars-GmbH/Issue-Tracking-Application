package com.example.application.data.entity;

import com.example.application.data.service.TimeFormatUtility;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.Formula;

import java.util.LinkedList;
import java.util.List;

// Entity in Progress
@Entity
public class Website extends AbstractEntity{
    @NotBlank
    private String website_name;
    @NotBlank
    private String URL;

    @OneToMany( mappedBy ="website", fetch = FetchType.EAGER)
    private List<Ticket> tickets = new LinkedList<>();

    @ManyToOne(targetEntity = TUser.class)
    @JoinColumn(name = "tuser_id")
    //@JsonIgnoreProperties({"websites"})
    private TUser tuser;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public Website(){}

    public Website(String name, String url){
        this.website_name = name;
        this.URL = url;
    }

    public String getWebsite_name() {
        return website_name;
    }

    public void setWebsite_name(String website_name) {
        this.website_name = website_name;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public TUser getUser() {
        return tuser;
    }

    public void setUser(TUser tuser) {
        this.tuser = tuser;
    }

    public void deleteUser(){
        this.tuser = null;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return website_name;
    }

    @Formula("(select count(t.id) from Ticket t where t.website_id = id)")
    private int ticketsCount;

    @Formula("(select count(t.id) from Ticket t where t.website_id = id and lower(t.status) not like lower('Solved') and lower(t.status) not like lower('Cancelled'))")
    private int openTicketsCount;

    @Formula("(select count(t.id) from Ticket t where t.website_id = id and lower(t.status) like lower('Solved'))")
    private int solvedTicketsCount;
    public int getTicketsCount(){
        return ticketsCount;
    }

    public int getOpenTicketsCount(){
        return openTicketsCount;
    }

    public int getSolvedTicketsCount(){
        return solvedTicketsCount;
    }

    public long getAverageSolveTime(){
        Long averageSolveTime = Long.valueOf(0);
        int solvedTicketsCount = 0;
        for (Ticket t : this.tickets){
            if (t.getStatus().equals("Solved")) {
                averageSolveTime += t.getTimeBetweenRegisteredAndSolved();
                solvedTicketsCount += 1;
            }
        }
        if (solvedTicketsCount == 0) return 0;
        else return averageSolveTime/solvedTicketsCount;
    }

    public String getAverageSolveTimeString(){
        return TimeFormatUtility.millisecondsToTimeFormat(this.getAverageSolveTime());
    }

}