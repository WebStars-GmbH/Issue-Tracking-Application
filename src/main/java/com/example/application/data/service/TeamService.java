package com.example.application.data.service;

import com.example.application.data.entity.*;
import com.example.application.data.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;


    public TeamService(
            TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> findAllTeams(String stringFilter){
        if (stringFilter == null || stringFilter.isEmpty()) {
            return teamRepository.findAll();
        } else {
            return teamRepository.search(stringFilter);
        }
    }

    public void deleteTeam(Team team) {
        teamRepository.delete(team);
    }

    public void saveTeam(Team team) {
        if (team == null) {
            System.err.println("Team is null. Are you sure you have connected your form to the application?");
            return;
        }
        teamRepository.save(team);
    }
}
