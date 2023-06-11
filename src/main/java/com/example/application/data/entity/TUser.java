package com.example.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.LinkedList;
import java.util.List;

@Entity
public class TUser extends AbstractEntity{

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirm;

    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;
    @NotBlank
    private String username;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @OneToMany (mappedBy="assigned_to")
    private List<Ticket> assigned_tickets = new LinkedList<>();

    @OneToMany(mappedBy = "tuser", fetch = FetchType.EAGER)
    private List<Website> websites = new LinkedList<>();

    @ManyToMany(mappedBy = "team_members", fetch = FetchType.EAGER)
    private List<Team> teams = new LinkedList<>();

    private boolean active = true;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Website> getWebsites() {
        return websites;
    }

    public void setWebsites(List<Website> websites) {
        this.websites = websites;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public List<Ticket> getAssigned_tickets() {
        return assigned_tickets;
    }

    public void setAssigned_tickets(List<Ticket> assigned_tickets) {
        this.assigned_tickets = assigned_tickets;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public TUser() {
    }

    public TUser(String firstname, String lastname, String name, String email, String password, Role role, List<Website> websiteList) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.websites = websiteList;
    }
}



