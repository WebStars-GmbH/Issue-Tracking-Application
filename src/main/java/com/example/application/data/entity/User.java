package com.example.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.LinkedList;
import java.util.List;

// Entity in Progress
@Entity(name="entity_user")
@Table(name="User")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @OneToMany(mappedBy="registered_by", fetch = FetchType.EAGER)
    private List<Ticket> registered_by = new LinkedList<>();

    @OneToMany(mappedBy="assigned_to", fetch = FetchType.EAGER)
    private List<Ticket> assigned_to = new LinkedList<>();

    @OneToMany(mappedBy="closed_by", fetch = FetchType.EAGER)
    private List<Ticket> closed_by = new LinkedList<>();
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String role;
    @NotBlank
    private String email;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Website> websites = new LinkedList<>();

    @ManyToMany(mappedBy = "team_members")
    private List<Team> teams = new LinkedList<>();

    public Long getId() {
        return user_id;
    }

    public void setId(Long id) {
        this.user_id = id;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
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

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public List<Ticket> getRegistered_by() {
        return registered_by;
    }

    public void setRegistered_by(List<Ticket> registered_by) {
        this.registered_by = registered_by;
    }

    public List<Ticket> getAssigned_to() {
        return assigned_to;
    }

    public void setAssigned_to(List<Ticket> assigned_to) {
        this.assigned_to = assigned_to;
    }

    public List<Ticket> getClosed_by() {
        return closed_by;
    }

    public void setClosed_by(List<Ticket> closed_by) {
        this.closed_by = closed_by;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public User() {
    }

    public User(String name, String email, String password, String role) {
        this.username = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}



