package com.pizzeria.service;

import com.pizzeria.model.entity.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class PedidoService {

    @Transactional
    public Pedido criar(Pedido pedido, User cliente) {
        pedido.setCliente(cliente);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setDataAtualizacao(LocalDateTime.now());
        pedido.setStatus(Pedido.Status.RECEBIDO);
        
        calcularValorTotal(pedido);
        pedido.persist();
        
        registrarLog(cliente, "CRIAR", "Pedido criado: #" + pedido.id);
        return pedido;
    }
    
    @Transactional
    public Pedido atualizarStatus(Long id, Pedido.Status novoStatus, User usuario) {
        Pedido pedido = Pedido.findById(id);
        if (pedido == null) {
            throw new RuntimeException("Pedido não encontrado");
        }
        
        pedido.setStatus(novoStatus);
        pedido.setDataAtualizacao(LocalDateTime.now());
        
        if (novoStatus == Pedido.Status.PREPARANDO) {
            pedido.setAtendente(usuario);
        }
        
        registrarLog(usuario, "ATUALIZAR", "Status do pedido #" + id + " atualizado para " + novoStatus);
        return pedido;
    }
    
    public List<Pedido> listarTodos() {
        return Pedido.listAll();
    }
    
    public List<Pedido> listarPorCliente(Long clienteId) {
        return Pedido.list("cliente.id", clienteId);
    }
    
    public List<Pedido> listarPorStatus(Pedido.Status status) {
        return Pedido.list("status", status);
    }
    
    public Pedido buscarPorId(Long id) {
        return Pedido.findById(id);
    }
    
    private void calcularValorTotal(Pedido pedido) {
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (ItemPedido item : pedido.getItens()) {
            valorTotal = valorTotal.add(item.getSubtotal());
        }
        pedido.setValorTotal(valorTotal);
    }
    
    private void registrarLog(User usuario, String acao, String descricao) {
        Log log = new Log();
        log.setUsuario(usuario);
        log.setAcao(acao);
        log.setDescricao(descricao);
        log.persist();
    }
} 