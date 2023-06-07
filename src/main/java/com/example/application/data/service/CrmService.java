package com.example.application.data.service;

import com.example.application.data.entity.*;
import com.example.application.data.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrmService {

    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;
    private final StatusRepository statusRepository;

    private final TicketRepository ticketRepository;

    private final WebsiteRepository websiteRepository;

    private final TUserRepository tUserRepository;

    public CrmService(ContactRepository contactRepository,
                      CompanyRepository companyRepository,
                      StatusRepository statusRepository,
                      TicketRepository ticketRepository,
                      WebsiteRepository websiteRepository,
                      TUserRepository tUserRepository) {
        this.contactRepository = contactRepository;
        this.companyRepository = companyRepository;
        this.statusRepository = statusRepository;
        this.ticketRepository = ticketRepository;
        this.websiteRepository = websiteRepository;
        this.tUserRepository = tUserRepository;
    }

    public List<Contact> findAllContacts(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return contactRepository.findAll();
        } else {
            return contactRepository.search(stringFilter);
        }
    }

    public long countContacts() {
        return contactRepository.count();
    }

    public void deleteContact(Contact contact) {
        contactRepository.delete(contact);
    }

    public void saveContact(Contact contact) {
        if (contact == null) {
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        contactRepository.save(contact);
    }

    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    public List<Status> findAllStatuses(){
        return statusRepository.findAll();
    }

    public List<Website> findAllWebsites(){
        return websiteRepository.findAll();
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

    public List<TUser> findAllTUsers(){return tUserRepository.findAll();}
    public List<TUser> findAllTUsers(String stringFilter){
        if (stringFilter == null || stringFilter.isEmpty()) {
            return tUserRepository.findAll();
        } else {
            return tUserRepository.search(stringFilter);
        }
    }

    public TUser getTUserByUsername(String stringFilter){
            return tUserRepository.getTUserByUsername(stringFilter);
    }

    public List<TUser> findAllTUsersByRole(String stringFilter){
        if (stringFilter == null || stringFilter.isEmpty()) {
            return tUserRepository.findAll();
        } else {
            return tUserRepository.searchByRole(stringFilter);
        }
    }

    public long countTUsers() {
        return tUserRepository.count();
    }

    public void deleteTUser(TUser tUser) {
        tUserRepository.delete(tUser);
    }

    public void saveTUser(TUser tUser) {
        if (tUser == null) {
            System.err.println("TUser is null. Are you sure you have connected your form to the application?");
            return;
        }
        tUserRepository.save(tUser);
    }
}