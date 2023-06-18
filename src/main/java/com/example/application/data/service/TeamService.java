package com.example.application.data.service;

import com.example.application.data.entity.*;
import com.example.application.data.repository.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
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

    public Team findTeamByName(String stringFilter){
        return teamRepository.getTeamByName(stringFilter);
    }

    public void deleteTeam(Team team) {
        if (!team.getWebsites().isEmpty()) {
            Notification notification = Notification
                    .show("Team cannot be deleted! Please control if there are websites assigned to this team.");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
        else {
            teamRepository.delete(team);
            Notification notification = Notification
                    .show("Team deleted!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }
    }

    public void saveTeam(Team team) {
        if (team == null) {
            System.err.println("Team is null. Are you sure you have connected your form to the application?");
            return;
        }
        teamRepository.save(team);
    }
}
