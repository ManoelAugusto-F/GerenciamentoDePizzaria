package com.pizzeria.model.dto.Pedido;

import lombok.Data;

@Data
public class ItemPedidoResponseDTO {
    private Long produtoId;
    private String nomeProduto;
    private Integer quantity;
    private double preco;
}
