package com.example.application.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.LinkedList;
import java.util.List;

@Entity
public class Role extends AbstractEntity {

    private String name;

    @OneToMany(mappedBy = "role")
    private List<TUser> users = new LinkedList<>();

    public Role() {}

    @Override
    public String toString() {
        return name;
    }

    public Role(String name) {
        this.name = name;
    }

    public String getRole_name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
