package com.example.application.views;

import com.example.application.data.entity.Team;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeamViewTest {
    @Autowired
    private TeamView teamView;

    @Test
    public void formShonWhenTeamSelected() {
        Grid<Team> grid = teamView.grid;
        Team firstTeam = getFirstItem(grid);

        TeamForm form = teamView.form;

        assertFalse(form.isVisible());
        grid.asSingleSelect().setValue(firstTeam);
        assertTrue(form.isVisible());
        assertEquals(firstTeam.getName(), form.name.getValue());
    }

    private Team getFirstItem(Grid<Team> grid) {
        return((ListDataProvider<Team>) grid.getDataProvider()).getItems().iterator().next();
    }
}