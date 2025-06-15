package com.pizzeria.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "pizzas")
public class Pizza extends PanacheEntity {
    @NotBlank
    public String name;

    public String description;

    @NotNull
    @Positive
    public Double price;

    public String imageUrl;

    @NotNull
    public Boolean available = true;
} 