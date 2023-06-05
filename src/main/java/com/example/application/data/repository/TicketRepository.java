package com.example.application.data.repository;

import com.example.application.data.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("select t from Ticket t " +
            "where lower(t.website.website_name) like lower(concat('%', :searchTerm, '%'))")
    List<Ticket> search(@Param("searchTerm") String searchTerm);
}
