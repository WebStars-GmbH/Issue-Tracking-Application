package com.example.application.views;

import com.example.application.data.entity.Ticket;
import com.example.application.data.entity.Website;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketAddFormTest {
    private List<Website> websites;
    private Ticket testTicket1;
    private Website website1;
    private Website website2;

    @BeforeEach
    public void setupData() {
        websites = new ArrayList<>();
        website1 = new Website();
        website1.setWebsite_name("testwebsite1.com");
        website1.setURL("www.testUrl.com");
        website2 = new Website();
        website2.setWebsite_name("testwebsite2.com");
        website2.setURL("www.testUrl2.com");
        websites.add(website1);
        websites.add(website2);

        testTicket1 = new Ticket();
        testTicket1.setHeader("Test Ticket 1");
        testTicket1.setDescription("This is a test ticket description");
        testTicket1.setWebsite(website2);
    }

    @Test
    public void formFieldsPopulated() {
        TicketAddForm form = new TicketAddForm(websites, null);
        form.setTicket(testTicket1);
        assertEquals("Test Ticket 1", form.header.getValue());
        assertEquals("This is a test ticket description", form.description.getValue());
        assertEquals(website2, form.website.getValue());
    }

    @Test
    public void saveEventHasCorrectValues() {
        TicketAddForm form = new TicketAddForm(websites, null);
        Ticket ticket = new Ticket();
        form.setTicket(ticket);
        form.header.setValue("Test Ticket 2");
        form.description.setValue("This is a test ticket description");
        form.website.setValue(website1);

        AtomicReference<Ticket> savedTicketRef = new AtomicReference<>(null);
        form.addSaveListener(e -> {
            savedTicketRef.set(e.getTicket());
        });
        form.save.click();
        Ticket savedTicket = savedTicketRef.get();

        assertEquals("Test Ticket 2", savedTicket.getHeader());
        assertEquals("This is a test ticket description", savedTicket.getDescription());
        assertEquals(website1, savedTicket.getWebsite());
    }
}