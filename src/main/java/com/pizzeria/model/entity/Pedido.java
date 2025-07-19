package com.pizzeria.model.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido extends PanacheEntity {
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private User cliente;
    
    @Column(nullable = false)
    private LocalDateTime dataPedido;
    
    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    
    @Column(nullable = false)
    private String enderecoEntrega;
    
    @Column(nullable = false)
    private String formaPagamento;
    
    @Column(nullable = false)
    private BigDecimal valorTotal;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "atendente_id")
    private User atendente;
    
    public enum Status {
        RECEBIDO,
        PREPARANDO,
        PRONTO,
        ENTREGUE
    }
    
    @PrePersist
    @PreUpdate
    public void atualizarDataAtualizacao() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    public User getCliente() { return cliente; }
    public void setCliente(User cliente) { this.cliente = cliente; }
    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public String getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(String enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }
    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }
    public User getAtendente() { return atendente; }
    public void setAtendente(User atendente) { this.atendente = atendente; }
} 