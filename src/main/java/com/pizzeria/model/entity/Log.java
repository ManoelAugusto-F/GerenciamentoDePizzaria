package com.pizzeria.model.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "logs")
@EqualsAndHashCode(callSuper = true)
public class Log extends PanacheEntity {
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Column(nullable = false)
    private LocalDateTime dataHora;
    
    @Column(nullable = false)
    private String acao;
    
    @Column(nullable = false)
    private String descricao;
    
    @Column
    private String detalhes;
    
    @PrePersist
    public void prePersist() {
        this.dataHora = LocalDateTime.now();
    }
} 