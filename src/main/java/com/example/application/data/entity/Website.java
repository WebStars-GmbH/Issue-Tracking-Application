package com.example.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

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

}
