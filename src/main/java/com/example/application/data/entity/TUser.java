package com.example.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.LinkedList;
import java.util.List;

// Entity in Progress
@Entity(name="entity_tuser")
@Table(name="TUser")
public class TUser extends AbstractEntity{
   /* @Id
    @Column(name = "tuser_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
*/
    @OneToMany(mappedBy="registered_by", fetch = FetchType.EAGER)
    private List<Ticket> registered_by = new LinkedList<>();

    @OneToMany(mappedBy="assigned_to", fetch = FetchType.EAGER)
    private List<Ticket> assigned_to = new LinkedList<>();

    @OneToMany(mappedBy="closed_by", fetch = FetchType.EAGER)
    private List<Ticket> closed_by = new LinkedList<>();
    @NotBlank
    private String email;

    @NotBlank
    private String password;
    @NotBlank
    private String role;

    @NotBlank
    private String username;


    /*
    @OneToMany(mappedBy="registered_by", fetch = FetchType.EAGER)
    private List<Ticket> registered_tickets = new LinkedList<>();
    */

    @OneToMany ( mappedBy="assigned_to")
    private List<Ticket> assigned_tickets = new LinkedList<>();

    /*
    @OneToMany(mappedBy="closed_by", fetch = FetchType.EAGER)
    private List<Ticket> closed_tickets = new LinkedList<>();
     */

    @OneToMany(mappedBy = "tuser", fetch = FetchType.EAGER)
    private List<Website> websites = new LinkedList<>();

    @ManyToMany(mappedBy = "team_members")
    private List<Team> teams = new LinkedList<>();

    /*public Long getId() {
        return user_id;
    }

    public void setId(Long id) {
        this.user_id = id;
    }
*/
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
    /*
        public List<Ticket> getRegistered_by() {
            return registered_tickets;
        }

   /* public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
*/
    public List<Ticket> getRegistered_by() {
        return registered_by;
    }

    public void setRegistered_by(List<Ticket> registered_by) {
        this.registered_by = registered_by;
    }

    public List<Ticket> getAssigned_to() {
        return assigned_tickets;
    }

    public void setAssigned_to(List<Ticket> assigned_to) {
        this.assigned_tickets = assigned_to;
    }
/*
    public List<Ticket> getClosed_by() {
        return closed_tickets;
    }

    public void setClosed_by(List<Ticket> closed_by) {
        this.closed_tickets = closed_by;
    }
 */

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public TUser() {
    }

    public TUser(String name, String email, String password, String role) {
        this.username = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}



