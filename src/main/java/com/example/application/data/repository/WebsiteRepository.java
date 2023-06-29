package com.example.application.data.repository;

import com.example.application.data.entity.TUser;
import com.example.application.data.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WebsiteRepository extends JpaRepository<Website, Long> {

    @Query("SELECT w FROM Website w WHERE lower(w.website_name) LIKE lower(concat('%', :websiteName, '%'))")
    List<Website> findByWebsiteName(@Param("websiteName") String websiteName);

    @Query("SELECT w FROM Website w WHERE w.tuser = :user")
    List<Website> findByUser(@Param("user") TUser user);

    @Query("SELECT w FROM Website w WHERE w.tuser IS NULL")
    List<Website> findByEmptyUser();

    @Query("SELECT w FROM Website w WHERE lower(w.tuser.username) LIKE lower(:username)")
    List<Website> findByUsername(@Param("username") String username);

}