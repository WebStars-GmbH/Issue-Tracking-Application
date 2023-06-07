package com.example.application.data.repository;

import com.example.application.data.entity.TUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TUserRepository extends JpaRepository<TUser, Long> {

    @Query("select t from TUser t " +
            "where lower(t.username) like lower(concat('%', :searchTerm, '%'))")
    List<TUser> search(@Param("searchTerm") String searchTerm);

    @Query("select t from TUser t " +
            "where lower(t.role) like lower(concat('%', :searchTerm, '%'))")
    List<TUser> searchByRole(@Param("searchTerm") String searchTerm);

    @Query("select t from TUser t " +
            "where lower(t.username) like lower(:searchTerm)")
    TUser getTUserByUsername(@Param("searchTerm") String searchTerm);
}

