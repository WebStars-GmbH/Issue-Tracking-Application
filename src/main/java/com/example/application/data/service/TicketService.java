package com.example.application.data.service;

import com.example.application.data.entity.*;
import com.example.application.data.repository.*;
import org.springframework.stereotype.Service;

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
            return ticketRepository.findAll();
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

    public List<Ticket> findAllTicketsByDescription(String stringFilter){
        if (stringFilter == null || stringFilter.isEmpty()) {
            return ticketRepository.findAll();
        } else {
            return ticketRepository.searchByDescription(stringFilter);
        }
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
        ticketRepository.save(ticket);
    }
}