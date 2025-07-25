package com.pizzeria.model.entity;

import com.pizzeria.Enum.StatusPedido;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
public class Pedido extends PanacheEntity {
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private User cliente;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;
    
    @Column(nullable = false)
    private double total;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "atendente_id")
    private User atendente;

} 