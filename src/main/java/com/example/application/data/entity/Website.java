package com.example.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.LinkedList;
import java.util.List;
// Entity in Progress
@Entity(name="entity_website")
@Table(name="Website")
public class Website {
    @Id
    @Column(name = "website_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long website_id;
    @NotBlank
    private String website_name;
    @NotBlank
    private String URL;

    @OneToMany ( mappedBy ="website")
    private List<Ticket> tickets = new LinkedList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public Website(){}

    public Website(String name, String url){
        this.website_name = name;
        this.URL = url;
    }

    public Long getId() {
        return website_id;
    }

    public void setId(Long id) {
        this.website_id = id;
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

    public Long getWebsite_id() {
        return website_id;
    }

    public void setWebsite_id(Long website_id) {
        this.website_id = website_id;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
