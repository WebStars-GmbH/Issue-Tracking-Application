package com.example.application.data.repository;

import com.example.application.data.entity.TUser;
import com.example.application.data.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TUserRepository extends JpaRepository<TUser, Long> {
    Optional<TUser> findByUsername(String username);

    @Query("select t from TUser t " +
            "where lower(t.username) like lower(concat('%', :searchTerm, '%'))")
    List<TUser> search(@Param("searchTerm") String searchTerm);

    @Query("select t from TUser t " +
            "where lower(t.role.name) like lower(concat('%', :searchTerm, '%'))")
    List<TUser> searchByRole(@Param("searchTerm") String searchTerm);

    @Query("select t from TUser t " +
            "where lower(t.username) like lower(:searchTerm)")
    TUser getTUserByUsername(@Param("searchTerm") String searchTerm);

    @Query("select u from TUser u WHERE (:searchTerm) MEMBER OF u.teams")
    List <TUser> getTUsersByTeam(@Param("searchTerm") Team searchTerm);

}

