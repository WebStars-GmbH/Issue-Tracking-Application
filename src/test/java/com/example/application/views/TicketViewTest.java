package com.example.application.views;

import com.example.application.data.entity.Ticket;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TicketViewTest {

    static {
        // Prevent Vaadin Development mode to launch browser window
        System.setProperty("vaadin.launch-browser", "false");
    }

    @Autowired
    private TicketView ticketView;

    @Test
    public void formShownWhenContactSelected() {
        Grid<Ticket> grid = ticketView.grid;
        Ticket firstTicket = getFirstItem(grid);

        TicketForm form = ticketView.form;

        assertFalse(form.isVisible());
        grid.asSingleSelect().setValue(firstTicket);
        assertTrue(form.isVisible());
        assertEquals(firstTicket.getHeader(), form.header.getValue());
    }

    private Ticket getFirstItem(Grid<Ticket> grid) {
        return( (ListDataProvider<Ticket>) grid.getDataProvider()).getItems().iterator().next();
    }
}