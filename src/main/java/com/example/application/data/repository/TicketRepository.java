package com.example.application.data.repository;

import com.example.application.data.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("select t from Ticket t " +
            "order by t.register_date ")
    List<Ticket> findAllOrderByRegisterDate();

    @Query ("select t from Ticket t " +
            "where lower(t.registered_by) = lower(concat(:registeredFilter)) " +
            "order by t.register_date ")
    List<Ticket> findAllByRegisteredBy(@Param("registeredFilter") String registeredFilter);


    @Query("select t from Ticket t " +
            "where lower(t.website.website_name) like lower(concat('%', :searchTerm, '%'))")
    List<Ticket> search(@Param("searchTerm") String searchTerm);

    @Query("select t from Ticket t " +
            "where lower(t.status) like lower(concat('%', :searchTerm, '%'))")
    List<Ticket> searchByStatus(@Param("searchTerm") String searchTerm);

    @Query("select t from Ticket t " +
            "where lower(t.assigned_to.username) like lower(concat('%', :searchTerm, '%'))")
    List<Ticket> searchByAssignedTo(@Param("searchTerm") String searchTerm);

    @Query("select t from Ticket t " +
            "where lower(t.description) like lower(concat('%', :searchTerm, '%'))")
    List<Ticket> searchByDescription(@Param("searchTerm") String searchTerm);

    @Query("select t from Ticket t " +
            "where lower(t.status) like lower(concat('%', :statusFilter, '%')) " +
            "and lower(t.website.website_name) like lower(concat('%', :websiteFilter, '%')) " +
            "and lower(t.description) like lower(concat('%', :descriptionFilter, '%')) and " +
            "lower(t.assigned_to.username) like lower(concat('%', :assignedToFilter, '%'))")
    List<Ticket> searchByAllFilters(@Param("statusFilter") String statusFilter, @Param("websiteFilter") String websiteFilter, @Param("descriptionFilter") String descriptionFilter, @Param("assignedToFilter") String assignedToFilter);

    @Query("select t from Ticket t " +
            "where lower(t.status) like lower(concat('%', :statusFilter, '%')) " +
            "and lower(t.website.website_name) like lower(concat('%', :websiteFilter, '%')) " +
            "and lower(t.description) like lower(concat('%', :descriptionFilter, '%'))")
    List<Ticket> searchByStatusWebsiteDescription(@Param("statusFilter") String statusFilter, @Param("websiteFilter") String websiteFilter, @Param("descriptionFilter") String descriptionFilter);

    @Query ("select t from Ticket t " +
            "where lower(t.registered_by) like lower(concat('%', :registeredFilter, '%'))" +
            "order by t.register_date ")
    List<Ticket> searchByRegisteredBy(@Param("registeredFilter") String registeredFilter);

    @Query("SELECT t FROM Ticket t " +
            "WHERE lower(t.assigned_to.username) = lower(:assignedFilter) " +
            "AND lower(t.status) IN (lower(:statusFilter1), lower(:statusFilter2), lower(:statusFilter3))")
    List<Ticket> searchByAssignedToAndStatus(@Param("assignedFilter") String assignedFilter,
                                             @Param("statusFilter1") String statusFilter1,
                                             @Param("statusFilter2") String statusFilter2,
                                             @Param("statusFilter3") String statusFilter3);

    @Query("select t from Ticket t " +
            "where lower(t.status) IN (lower(:statusFilter1), lower(:statusFilter2), lower(:statusFilter3))")
    List<Ticket> searchByStatus(@Param("statusFilter1") String statusFilter1,
                                @Param("statusFilter2") String statusFilter2,
                                @Param("statusFilter3") String statusFilter3);

    @Query("select t from Ticket t " +
            "where lower(t.status) like lower(concat('%', :searchTerm, '%'))")
    List<Ticket> searchWithStatus(@Param("searchTerm") String searchTerm);

    @Query("SELECT t FROM Ticket t " +
            "WHERE lower(t.registered_by) = lower(:registeredFilter) " +
            "AND lower(t.status) IN (lower(:statusFilter1), lower(:statusFilter2), lower(:statusFilter3))" +
            "ORDER BY t.register_date ")
    List<Ticket> searchByRegisteredByStatus(@Param("registeredFilter") String registeredFilter,
                                            @Param("statusFilter1") String statusFilter1,
                                            @Param("statusFilter2") String statusFilter2,
                                            @Param("statusFilter3") String statusFilter3);

    @Query("SELECT t FROM Ticket t " +
            "WHERE lower(t.registered_by) = lower(:registeredFilter) " +
            "and lower(t.description) like lower(concat('%', :descriptionFilter, '%'))")
    List<Ticket> searchByDescriptionAndRegisteredBy(@Param("descriptionFilter") String descriptionFilter,
                                            @Param("registeredFilter") String registeredFilter);

    @Query("select t from Ticket t " +
            "where t.id = :searchTerm")
    Ticket getTicketById(@Param("searchTerm") Long searchTerm);

    @Query ("select t from Ticket t " +
            "where lower(t.status) like lower('Registered') " +
            "or lower(t.status) like lower('Assigned') " +
            "or lower(t.status) like lower('In progress') ")
    List<Ticket> searchByOpenStatus();

    @Query("select count(t) from Ticket t where lower(t.assigned_to.username) like lower(:assignedToFilter)")
    int ticketCountByAssignedTo(@Param("assignedToFilter")String assignedToFilter);
    @Query("select count(t) from Ticket t where lower(t.assigned_to.username) like lower(:assignedToFilter) and lower(t.status) like lower(:statusFilter)")
    int ticketCountByAssignedToAndStatus(@Param("assignedToFilter")String assignedToFilter, @Param("statusFilter")String statusFilter);

    @Query("select count(t) from Ticket t where lower(t.assigned_to.username) like lower(:assignedToFilter) and t.status not like ('Solved') and t.status not like ('Cancelled')")
    int openTicketCountByAssignedTo(@Param("assignedToFilter")String assignedToFilter);
}