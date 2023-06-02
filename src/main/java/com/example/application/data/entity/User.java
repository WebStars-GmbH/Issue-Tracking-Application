package com.example.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.LinkedList;
import java.util.List;

@Entity(name="entity_user")
@Table(name="User")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String role;
    @NotBlank
    private String email;

    private List<Website> websites = new LinkedList<>();

    public User() {
    }

    public User(Long id, String name, String email, String password, String role) {
        this.id = id;
        this.username = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}



