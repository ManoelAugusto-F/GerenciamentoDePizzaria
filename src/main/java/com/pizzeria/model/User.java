package com.pizzeria.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User extends PanacheEntity {
    @NotBlank
    @Size(min = 2, max = 100)
    public String name;

    @NotBlank
    @Email
    @Column(unique = true)
    public String email;

    @NotBlank
    @Size(min = 8)
    public String password;

    @NotBlank
    @Column(name = "roles")
    public String roles; // e.g. "USER", "ADMIN"
} 