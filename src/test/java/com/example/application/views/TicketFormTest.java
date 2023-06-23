//package com.example.application.views;
//
//import com.example.application.data.entity.TUser;
//import com.example.application.data.entity.Ticket;
//import com.example.application.data.entity.Website;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicReference;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class TicketFormTest {
//    private List<Website> websites;
//    private List<TUser> tusers;
//    private Ticket testTicket1;
//    private Website website1;
//    private Website website2;
//    private TUser tuser1;
//    private TUser tuser2;
//
//    @BeforeEach
//    public void setupData() {
//        websites = new ArrayList<>();
//        website1 = new Website();
//        website1.setWebsite_name("testwebsite1.com");
//        website1.setURL("www.testUrl.com");
//        website2 = new Website();
//        website2.setWebsite_name("testwebsite2.com");
//        website2.setURL("www.testUrl2.com");
//        websites.add(website1);
//        websites.add(website2);
//
//        tusers = new ArrayList<>();
//        tuser1 = new TUser();
//        tuser1.setUsername("testuser1");
//        tuser2 = new TUser();
//        tuser2.setUsername("testuser2");
//        tusers.add(tuser1);
//        tusers.add(tuser2);
//
//        testTicket1 = new Ticket();
//        testTicket1.setHeader("Test Ticket 1");
//        testTicket1.setDescription("This is a test ticket description");
//        testTicket1.setSolution("This is a test ticket solution");
//        testTicket1.setStatus("Test Status");
//        testTicket1.setPriority(2);
//        testTicket1.setAssigned_to(tuser1);
//        testTicket1.setWebsite(website2);
//    }
//
//    @Test
//    public void formFieldsPopulated() {
//        TicketForm form = new TicketForm( websites, tusers);
//        form.setTicket(testTicket1);
//        assertEquals("Test Ticket 1", form.header.getValue());
//        assertEquals("This is a test ticket description", form.description.getValue());
//        assertEquals("This is a test ticket solution", form.solution.getValue());
//        assertEquals("Test Status", form.status.getValue());
//        assertEquals(2, form.priority.getValue());
//        assertEquals(website2, form.website.getValue());
//        assertEquals(tuser1, form.assigned_to.getValue());
//    }
//
//    @Test
//    public void saveEventHasCorrectValues() {
//        TicketForm form = new TicketForm(websites, tusers);
//        Ticket ticket = new Ticket();
//        form.setTicket(ticket);
//        form.header.setValue("Test Ticket 2");
//        form.description.setValue("This is a test ticket description");
//        form.solution.setValue("This is a test ticket solution");
//        form.status.setValue("Test Status 2");
//        form.priority.setValue(1);
//        form.website.setValue(website1);
//        form.assigned_to.setValue(tuser2);
//
//        AtomicReference<Ticket> savedTicketRef = new AtomicReference<>(null);
//        form.addSaveListener(e -> {
//            savedTicketRef.set(e.getTicket());
//        });
//        form.save.click();
//        Ticket savedTicket = savedTicketRef.get();
//
//        assertEquals("Test Ticket 2", savedTicket.getHeader());
//        assertEquals("This is a test ticket description", savedTicket.getDescription());
//        assertEquals("This is a test ticket solution", savedTicket.getSolution());
//        assertEquals("Test Status 2", savedTicket.getStatus());
//        assertEquals(1, savedTicket.getPriority());
//        assertEquals(website1, savedTicket.getWebsite());
//        assertEquals(tuser2, savedTicket.getAssigned_to());
//    }
//
//    @Test
//    public void deleteEventHasCorrectValues() {
//        TicketForm form = new TicketForm(websites, tusers);
//        Ticket ticket = new Ticket();
//        form.setTicket(ticket);
//        form.header.setValue("Test Ticket 3");
//        form.description.setValue("This is a test ticket description");
//
//        AtomicReference<Ticket> deletedTicketRef = new AtomicReference<>(null);
//        form.addDeleteListener(e -> {
//            deletedTicketRef.set(e.getTicket());
//        });
//        form.delete.click();
//        Ticket deletedTicket = deletedTicketRef.get();
//
//        assertEquals("Test Ticket 3", deletedTicket.getHeader());
//        assertEquals("This is a test ticket description", deletedTicket.getDescription());
//    }
//}