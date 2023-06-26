package com.example.application.data.service;

import com.example.application.data.entity.Ticket;
import com.example.application.data.repository.TicketRepository;
import com.example.application.views.MainLayout;
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

    public List<Ticket> findAllOpenTickets(){
        return ticketRepository.searchByOpenStatus();
    }


    public Ticket getTicket(Long id){
        return ticketRepository.getTicketById(id);
    }
    public long countTickets() {
        return ticketRepository.count();
    }

    public void deleteTicket(Ticket ticket) {
        ticketRepository.delete(ticket);
    }

    public void setTicketStatusToCancelled(Ticket ticket) {
        ticket = ticketRepository.getTicketById(ticket.getId()); //To reset to ticket values before edit...
        ticket.setStatus("Cancelled");
        ticketRepository.save(ticket);
    }

    public void saveTicket(Ticket ticket) {
        if (ticket == null) {
            System.err.println("Ticket is null. Are you sure you have connected your form to the application?");
            return;
        }

        //Save last update timestamp
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ticket.setLast_update(timestamp);
        String timestampString = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(timestamp);
        String u = com.example.application.views.MainLayout.username;

        addToHistory(ticket, timestampString + "\tModified by: " + u + " \n");
        addToHistory(ticket, "Status: " + ticket.getStatus() + "\tPriority: " + ticket.getPriority());

        //Check if the ticket already exists
        Ticket oldTicket = ticketRepository.getTicketById(ticket.getId());
        if (oldTicket != null) {
            if (ticket.getAssigned_to() != null) addToHistory(ticket,"\tAssigned to " + ticket.getAssigned_to().getUsername());

            //If description has changed, add line with description to history
            if (!oldTicket.getDescription().equals(ticket.getDescription()) && ticket.getDescription().length() > 1) {
                String DescrStr = ticket.getDescription().replaceAll("(\\r|\\n)", "\t");
                addToHistory(ticket, "\nNotes: '" + DescrStr + "'");}

            //If ticket is solved, add line with solution to history
            if (!oldTicket.getSolution().equals(ticket.getSolution()) && ticket.getSolution().length() > 1) {
                String SolutStr = ticket.getSolution().replaceAll("(\\r|\\n)", "\t");
                addToHistory(ticket, "\nSolution: '" + SolutStr + "'");}
            addToHistory(ticket, "\n\n");

            //Check if status has changed to assigned or solved, and update the timestamps accordingly
            if (!oldTicket.getStatus().equals("Assigned") && ticket.getStatus().equals("Assigned")) {ticket.setAssign_date(timestamp);}
            if (!oldTicket.getStatus().equals("Solved") && ticket.getStatus().equals("Solved")) {
                ticket.setClose_date(timestamp);
                ticket.setClosed_by(MainLayout.username);
                addToHistory(ticket, "\nAssign time: " + TimeFormatUtility.millisecondsToTimeFormat(ticket.getTimeBetweenAssignedAndSolved()));
                addToHistory(ticket, "\nSolve time: " + TimeFormatUtility.millisecondsToTimeFormat(ticket.getTimeBetweenRegisteredAndSolved()));
                addToHistory(ticket, "\nSolved by: " + ticket.getClosed_by());
            }
        }

        ticketRepository.save(ticket);
        Notification notification = Notification
                .show("Ticket '" + ticket.getHeader() + "' saved!");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    public void addToHistory(Ticket ticket, String string){
        ticket.setHistory(ticket.getHistory() + string);
    }

    public List<Ticket> searchTicketsByStatus(String name, String status1, String status2, String status3) {
        return ticketRepository.searchByRegisteredByStatus(name, status1, status2, status3);
    }

    public List<Ticket> findAllTicketsByStatus(String status1, String status2, String status3) {
        return ticketRepository.searchByStatus(status1, status2, status3);
    }

    public List<Ticket> findAllTicketsByAssignedToAndStatus(String username, String status1, String status2, String status3) {
        return ticketRepository.searchByAssignedToAndStatus(username, status1, status2, status3);
    }

    public List<Ticket> searchTicketsByUserAndStatus(String name, String status1, String status2, String status3) {
        return ticketRepository.searchByRegisteredByStatus(name, status1, status2, status3);
    }
}