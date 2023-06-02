package com.example.application.data.entity;

import jakarta.persistence.*;

@Entity(name="entity_Team")
@Table(name="Team")
public class Team {
    @Id
    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long team_id;

    private Website websites;

    private User team_members;
}
