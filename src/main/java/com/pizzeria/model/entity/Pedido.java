package com.pizzeria.model.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "pedidos")
@EqualsAndHashCode(callSuper = true)
public class Pedido extends PanacheEntity {
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;
    
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
    private Usuario atendente;
    
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
} 