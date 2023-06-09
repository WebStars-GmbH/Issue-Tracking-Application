package com.example.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.LinkedList;
import java.util.List;

@Entity
public class Role extends AbstractEntity {

    @OneToMany(mappedBy = "role")
    private List<TUser> users = new LinkedList<>();

    private String name;

    public Role() {

    }

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
