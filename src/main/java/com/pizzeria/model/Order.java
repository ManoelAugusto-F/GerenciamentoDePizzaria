package com.pizzeria.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends PanacheEntity {
    @ManyToOne
    @NotNull
    public User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<OrderItem> items;

    @NotNull
    public Double total;

    public LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    public String status; // e.g. "PENDING", "PAID", "CANCELLED"
} 