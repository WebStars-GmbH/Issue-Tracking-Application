package com.example.application.data.service;

import com.example.application.data.entity.Ticket;
import com.example.application.data.repository.TicketRepository;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;


    public TicketService(
                      TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> findAllTickets(String stringFilter){
        if (stringFilter == null || stringFilter.isEmpty()) {
            return ticketRepository.findAll();
        } else {
            return ticketRepository.search(stringFilter);
        }
    }

    public List<Ticket> findAllTicketsByStatus(String stringFilter){
        if (stringFilter == null || stringFilter.isEmpty()) {
            return ticketRepository.searchByRegisteredBy(stringFilter);
        } else {
            return ticketRepository.searchByStatus(stringFilter);
        }
    }

    public List<Ticket> findAllTicketsByAssignedTo(String stringFilter){
        if (stringFilter == null || stringFilter.isEmpty()) {
            return ticketRepository.findAll();
        } else {
            return ticketRepository.searchByAssignedTo(stringFilter);
        }
    }

    public List<Ticket> findAllTicketsByRegisteredBy(String stringFilter){
        if (stringFilter == null || stringFilter.isEmpty()) {
            return ticketRepository.findAll();
        } else {
            return ticketRepository.searchByRegisteredBy(stringFilter);
        }
    }

    public List<Ticket> findAllTicketsByDescription(String stringFilter){
        if (stringFilter == null || stringFilter.isEmpty()) {
            return ticketRepository.findAll();
        } else {
            return ticketRepository.searchByDescription(stringFilter);
        }
    }

    public List<Ticket> findAllTicketsByAllFilters(String statusFilter, String websiteFilter, String descriptionFilter, String assignedToFilter){
        return ticketRepository.searchByAllFilters(statusFilter, websiteFilter, descriptionFilter, assignedToFilter);
    }

    public List<Ticket> findAllTicketsByStatusWebsiteDescription(String statusFilter, String websiteFilter, String descriptionFilter){
        return ticketRepository.searchByStatusWebsiteDescription(statusFilter, websiteFilter, descriptionFilter);
    }


    public long countTickets() {
        return ticketRepository.count();
    }

    public void deleteTicket(Ticket ticket) {
        ticketRepository.delete(ticket);
    }

    public void saveTicket(Ticket ticket) {
        if (ticket == null) {
            System.err.println("Ticket is null. Are you sure you have connected your form to the application?");
            return;
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ticket.setLast_update(timestamp);
        String timestampString = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(timestamp);
        String u = com.example.application.views.MainLayout.username;
        //addToHistory(ticket, "---Begin of Entry--- ");
        addToHistory(ticket, timestampString + "\tModified by: " + u + " \n");
        addToHistory(ticket, "Status: " + ticket.getStatus() + "\tPriority: " + ticket.getPriority());
        if (ticket.getAssigned_to() != null) addToHistory(ticket,"\tAssigned to " + ticket.getAssigned_to().getUsername());
        if (ticket.getDescription().length() > 1) {
            String DescrStr = ticket.getDescription().replaceAll("(\\r|\\n)", "\t");
            addToHistory(ticket, "\nNotes: '" + DescrStr + "'");}
        if (ticket.getStatus() == "Solved" && ticket.getSolution().length() > 1) {
            String SolutStr = ticket.getSolution().replaceAll("(\\r|\\n)", "\t");
            addToHistory(ticket, "\nSolution: '" + SolutStr + "'");}
        addToHistory(ticket, "\n\n");
        //addToHistory(ticket, "\n---End of Entry---\n\n");

        ticketRepository.save(ticket);
        Notification notification = Notification
                .show("Ticket '" + ticket.getHeader() + "' saved!");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    public void addToHistory(Ticket ticket, String string){
        ticket.setHistory(ticket.getHistory() + string);
    }

    public List<Ticket> searchTicketsByStatus(String name, String status) {
        return ticketRepository.searchByRegisteredByStatus(name, status);
    }
}