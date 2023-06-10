package com.example.application.data.repository;

import com.example.application.data.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("select t from Team t " +
            "where lower(t.name) like lower(concat('%', :searchTerm, '%'))")
    List<Team> search(@Param("searchTerm") String searchTerm);
}
