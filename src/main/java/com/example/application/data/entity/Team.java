package com.example.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.LinkedList;
import java.util.List;

@Entity
public class Team extends AbstractEntity{
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
    private List<Website> websites = new LinkedList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "TeamMembersTeams",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="team_id")})
    private List<TUser> team_members = new LinkedList<>();

    public Team(){}

    public Team(String name){
        this.name = name;
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

    public List<TUser> getTeam_members() {
        return team_members;
    }

    public void setTeam_members(List<TUser> team_members) {
        this.team_members = team_members;
    }
}