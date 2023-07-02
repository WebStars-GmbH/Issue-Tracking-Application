package com.example.application.views;

import com.example.application.data.entity.Ticket;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CompanyTicketViewTest {
    @Qualifier("ticketView")
    @Autowired
    private TicketView TicketView;

    @Test
    public void formShownWhenCompanyTicketSelected() {
        Grid<Ticket> grid = TicketView.grid;
        Ticket firstCompanyTicket = getFirstItem(grid);

        TicketForm form = TicketView.form;

        assertFalse(form.isVisible());
        grid.asSingleSelect().setValue(firstCompanyTicket);
        assertTrue(form.isVisible());
        assertEquals(firstCompanyTicket.getHeader(), form.header.getValue());
    }

    private Ticket getFirstItem(Grid<Ticket> grid) {
        return((ListDataProvider<Ticket>) grid.getDataProvider()).getItems().iterator().next();
    }


}

