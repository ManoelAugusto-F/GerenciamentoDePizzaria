package com.pizzeria.model.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "produtos")
@EqualsAndHashCode(callSuper = true)
public class Produto extends PanacheEntity {
    
    @Column(nullable = false)
    private String nome;
    
    @Column(length = 1000)
    private String descricao;
    
    @Column(nullable = false)
    private BigDecimal preco;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo tipo;
    
    @Column(nullable = false)
    private boolean disponivel = true;
    
    @Column
    private String imagemUrl;
    
    public enum Tipo {
        PIZZA,
        BEBIDA,
        EXTRAS
    }
} 