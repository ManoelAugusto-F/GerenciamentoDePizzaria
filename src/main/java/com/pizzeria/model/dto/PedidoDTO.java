package com.pizzeria.model.dto;

import com.pizzeria.model.entity.ItemPedido;
import lombok.Data;

import java.util.List;

@Data
public class PedidoDTO {
    private List<ProdutoItem> produtos;
    private Double total;

}



