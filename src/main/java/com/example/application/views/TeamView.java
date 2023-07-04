package com.example.application.views;

import com.example.application.data.entity.TUser;
import com.example.application.data.entity.Team;
import com.example.application.data.service.TUserService;
import com.example.application.data.service.TeamService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;

import java.util.stream.Collectors;

@org.springframework.stereotype.Component
@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "Teams", layout = com.example.application.views.MainLayout.class)
@PageTitle("Teams | Webst@rs Ticketing Application")
public class TeamView extends VerticalLayout {
    Grid<Team> grid = new Grid<>(Team.class);
    TeamForm form;

    TeamService teamService;

    TUserService tUserService;

    public TeamView(TeamService teamService, TUserService tUserService) {
        this.teamService = teamService;
        this.tUserService = tUserService;
        addClassName("team-view");
        setSizeFull();
        configureGrid();
        configureForm();

        //if (tUserService.findUserByUsername(MainLayout.username).getRole().getRole_name().equals("System-Admin") || tUserService.findUserByUsername(MainLayout.username).getRole().getRole_name().equals("Support-Coordinator")) add(getToolbar(), getContent());
        //else add(getContent()); //UNCOMMENT AND DELETE NEXT LINE IF ONLY SYSADMIN AND COORDINATOR CAN ADD TEAMS...
        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new TeamForm(tUserService.findAllTUsersByRole("Support-Member"));
        form.setWidth("70em");
        form.addSaveListener(this::saveTeam); // <1>
        form.addDeleteListener(this::deleteTeam); // <2>
        form.addCloseListener(e -> closeEditor()); // <3>
    }

    private void saveTeam(TeamForm.SaveEvent event) {
        teamService.saveTeam(event.getTeam());
        updateList();
        closeEditor();
    }

    private void ConfirmAndDelete(Team team){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Do you want to delete this Team?");
        dialog.setText("Are you sure you want to permanently delete this ticket? This cannot be reversed.");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete Team");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> {
            teamService.deleteTeam(team);
            updateList();
            form.setTeam(null);
            form.setVisible(false);});
        dialog.open();
    }

    private void deleteTeam(TeamForm.DeleteEvent event) {
        teamService.deleteTeam(event.getTeam());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("team-grid");
        grid.setSizeFull();
        grid.setColumns("name");
        grid.addColumn(team -> team.getTeam_members().stream()
                        .map(TUser::getUsername)
                        .collect(Collectors.joining(", ")))
                .setHeader("Members");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        //if (tUserService.findUserByUsername(MainLayout.username).getRole().getRole_name().equals("System-Admin") || tUserService.findUserByUsername(MainLayout.username).getRole().getRole_name().equals("Support-Coordinator")) //UNCOMMENT IF ONLY SYSADMIN AND COORDINATOR CAN EDIT TEAMS...
        grid.asSingleSelect().addValueChangeListener(event -> editTeam(event.getValue()));
    }

    private Component getToolbar() {
        Button addTeamButton = new Button("Add Team");
        addTeamButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addTeamButton.addClickListener(click -> addTeam());

        var toolbar = new HorizontalLayout(addTeamButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editTeam(Team ticket) {
        if (ticket == null) {
            closeEditor();
        } else {
            closeEditor();
            form.setTeam(ticket);
            form.setVisible(true);
            addClassName("editing");

        }
    }

    private void closeEditor() {
        form.setTeam(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addTeam() {
        closeEditor();
        grid.asSingleSelect().clear();
        Team ticket = new Team();

        form.setTeam(ticket);
        form.setVisible(true);
        editTeam(ticket);
    }

    private void updateList() {
        grid.setItems(teamService.findAllTeams(""));
    }
}

