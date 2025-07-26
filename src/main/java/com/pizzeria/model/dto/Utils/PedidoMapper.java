package com.pizzeria.model.dto.Utils;

import com.pizzeria.model.dto.Pedido.ItemPedidoResponseDTO;
import com.pizzeria.model.dto.Pedido.PedidoResponseDTO;
import com.pizzeria.model.entity.Pedido;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
@ApplicationScoped
public class PedidoMapper {
    public static PedidoResponseDTO toDTO(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setStatus(pedido.getStatus().name());
        dto.setTotal(pedido.getTotal());

        List<ItemPedidoResponseDTO> itens = pedido.getItens().stream().map(item -> {
            ItemPedidoResponseDTO i = new ItemPedidoResponseDTO();
            i.setProdutoId(item.getProduto().getId());
            i.setNomeProduto(item.getProduto().getNome());
            i.setQuantity(item.getQuantity());
            i.setPreco(item.getPreco());
            return i;
        }).toList();

        dto.setItens(itens);
        return dto;
    }
}
