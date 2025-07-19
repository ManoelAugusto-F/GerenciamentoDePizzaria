package com.pizzeria.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderItemDTO {
    @NotNull
    public Long pizzaId;

    @NotNull
    @Positive
    public Integer quantity;
} 