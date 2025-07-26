package com.pizzeria.model.dto.Pedido;

import lombok.Data;

import java.util.List;
@Data
public class PedidoResponseDTO {
    private Long id;
    private String status;
    private Double total;
    private List<ItemPedidoResponseDTO> itens;
}
