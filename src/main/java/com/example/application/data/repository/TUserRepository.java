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


    // Suche searchterm caseinsensitiv in User entweder oder  (firstname, lastname, username, email, role, websites)

    @Query("SELECT u FROM TUser u WHERE " +
            "(LOWER(u.firstname) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.lastname) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) OR " +
            "EXISTS (SELECT w FROM u.websites w WHERE LOWER(w.website_name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) OR " +
            "EXISTS (SELECT r FROM u.role r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<TUser> searchBySearchTerm(@Param("searchTerm") String searchTerm);







}

