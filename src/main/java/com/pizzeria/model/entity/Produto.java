package com.pizzeria.model.entity;

import com.pizzeria.Enum.Tipo;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
@Data
@EqualsAndHashCode(callSuper = false)
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
    
    @Column(name = "imagem_url")
    private String imagemUrl;

    public Long getId(){
        return this.id;
    }
    
} 