package com.pizzeria.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public class OrderCheckoutDTO {
    @NotNull
    public List<OrderItemDTO> items;

    @NotNull
    public String paymentMethod; // e.g. "CREDIT_CARD", "PIX", "BOLETO"
} 