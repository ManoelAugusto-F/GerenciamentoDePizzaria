package com.pizzeria.service;

import com.pizzeria.Enum.StatusPedido;
import com.pizzeria.model.dto.Pedido.ItemPedidoResponseDTO;
import com.pizzeria.model.dto.Pedido.PedidoResponseDTO;
import com.pizzeria.model.entity.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class PedidoService {
@Inject
LogService logService;

    @Transactional
    public Pedido criar(Pedido pedido, User cliente) {
        pedido.persist();
        logService.registrarLog(cliente, "CRIAR", "Pedido criado: #");
        return pedido;
    }

    @Transactional
    public PedidoResponseDTO atualizarStatus(Long id, StatusPedido novoStatus, User usuario) {
        Pedido pedido = Pedido.findById(id);
        if (pedido == null) {
            throw new RuntimeException("Pedido não encontrado");
        }

        pedido.setStatus(novoStatus);
        logService.registrarLog(usuario, "ATUALIZAR", "Status do pedido #" + id + " atualizado para " + novoStatus);

        // monta o DTO aqui, ainda com a sessão aberta
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setStatus(pedido.getStatus().name());
        dto.setTotal(pedido.getTotal());

        List<ItemPedidoResponseDTO> itens = pedido.getItens().stream().map(item -> {
            ItemPedidoResponseDTO itemDto = new ItemPedidoResponseDTO();
            itemDto.setProdutoId(item.getProduto().getId());
            itemDto.setNomeProduto(item.getProduto().getNome());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPreco(item.getPreco());
            return itemDto;
        }).toList();

        dto.setItens(itens);
        return dto;
    }


    public List<PedidoResponseDTO> listarTodos() {
        List<Pedido> pedidos = Pedido.listAll();

        return pedidos.stream().map(pedido -> {
            PedidoResponseDTO dto = new PedidoResponseDTO();
            dto.setId(pedido.getId());
            dto.setStatus(pedido.getStatus().name());
            dto.setTotal(pedido.getTotal());

            List<ItemPedidoResponseDTO> itens = pedido.getItens().stream().map(item -> {
                ItemPedidoResponseDTO itemDto = new ItemPedidoResponseDTO();
                itemDto.setProdutoId(item.getProduto().getId());
                itemDto.setNomeProduto(item.getProduto().getNome()); // opcional
                itemDto.setQuantity(item.getQuantity());
                itemDto.setPreco(item.getPreco());
                return itemDto;
            }).toList();

            dto.setItens(itens);
            return dto;
        }).toList();
    }

    @Transactional
    public List<Pedido> listarPorCliente(Long clienteId) {
        return Pedido.list("cliente.id", clienteId);
    }
    @Transactional
    public List<Pedido> listarPorStatus(StatusPedido status) {
        return Pedido.list("status", status);
    }
    @Transactional
    public Pedido buscarPorId(Long id) {
        return Pedido.findById(id);
    }

} 