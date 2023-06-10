package com.example.application.views;

import com.example.application.data.entity.Team;
import com.example.application.data.service.CrmService;
import com.example.application.data.service.TeamService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "Teams", layout = com.example.application.views.MainLayout.class)
@PageTitle("Teams | Webst@rs Ticketing Application")
public class TeamView extends VerticalLayout {
    Grid<Team> grid = new Grid<>(Team.class);
    //TextField websiteFilterText = new TextField("Website"); //TODO

    TeamForm form;
    CrmService service;
    TeamService teamService;

    public TeamView(CrmService service, TeamService teamService) {
        this.service = service;
        this.teamService = teamService;
        addClassName("team-view");
        setSizeFull();
        configureGrid();
        configureForm();

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
        form = new TeamForm(service.findAllWebsites(), service.findAllTUsers());
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
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editTeam(event.getValue()));
    }

    private Component getToolbar() {
        //websiteFilterText.setPlaceholder("Filter by website...");
        //websiteFilterText.setTooltipText("Please type what the website name should contain...");
        //websiteFilterText.setClearButtonVisible(true);
        //websiteFilterText.setValueChangeMode(ValueChangeMode.LAZY);
        //websiteFilterText.addValueChangeListener(e -> updateList());

        Button addTeamButton = new Button("Add team");
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

