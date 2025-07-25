package com.pizzeria.service;

import com.pizzeria.Enum.StatusPedido;
import com.pizzeria.model.entity.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class PedidoService {
@Inject
LogService logService;

    @Transactional
    public Pedido criar(Pedido pedido, User cliente) {
        pedido.persist();
        logService.registrarLog(cliente, "CRIAR", "Pedido criado: #" + pedido.id);
        return pedido;
    }
    
    @Transactional
    public Pedido atualizarStatus(Long id, StatusPedido novoStatus, User usuario) {
        Pedido pedido = Pedido.findById(id);
        if (pedido == null) {
            throw new RuntimeException("Pedido não encontrado");
        }
        
        pedido.setStatus(novoStatus);
        pedido.setAtendente(usuario);

        logService.registrarLog(usuario, "ATUALIZAR", "Status do pedido #" + id + " atualizado para " + novoStatus);
        return pedido;
    }
    @Transactional
    public List<Pedido> listarTodos() {
        return Pedido.listAll();
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