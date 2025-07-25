package com.pizzeria.model.dto;

import lombok.Data;

@Data
public class ProdutoItem {
    private Long productId;
    private Integer quantity;
    private Integer preco;
}
