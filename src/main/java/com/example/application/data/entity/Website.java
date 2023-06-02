package com.example.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.LinkedList;
import java.util.List;
// Entity in Progress
@Entity(name="entity_website")
@Table(name="Website")
public class Website {
    @Id
    @Column(name = "website_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long website_id;
    @NotBlank
    private String website_name;
    @NotBlank
    private String URL;
    @OneToMany ( mappedBy ="website")
    private List<Ticket> tickets = new LinkedList<>();

    public Long getId() {
        return website_id;
    }

    public void setId(Long id) {
        this.website_id = id;
    }

    public String getWebsite_name() {
        return website_name;
    }

    public void setWebsite_name(String website_name) {
        this.website_name = website_name;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }



}
