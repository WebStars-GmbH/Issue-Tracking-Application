package com.example.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.LinkedList;
import java.util.List;

@Entity(name="entity_team")
@Table(name="Team")
public class Team {
    @Id
    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long team_id;

    @NotBlank
    private String name;

    @OneToMany(mappedBy = "team")
    private List<Website> websites = new LinkedList<>();

    @ManyToMany
    @JoinTable(name = "TeamMembersTeams",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="team_id")})
    private List<User> team_members = new LinkedList<>();

    public Team(){}

    public Team(String name){
        this.name = name;
    }
    public Long getTeam_id() {
        return team_id;
    }

    public void setTeam_id(Long team_id) {
        this.team_id = team_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Website> getWebsites() {
        return websites;
    }

    public void setWebsites(List<Website> websites) {
        this.websites = websites;
    }

    public List<User> getTeam_members() {
        return team_members;
    }

    public void setTeam_members(List<User> team_members) {
        this.team_members = team_members;
    }
}
