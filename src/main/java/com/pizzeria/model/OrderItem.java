package com.pizzeria.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "order_items")
public class OrderItem extends PanacheEntity {
    @ManyToOne
    @NotNull
    public Order order;

    @ManyToOne
    @NotNull
    public Pizza pizza;

    @NotNull
    @Positive
    public Integer quantity;

    @NotNull
    @Positive
    public Double price;
} 