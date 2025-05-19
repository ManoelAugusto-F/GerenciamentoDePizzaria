package com.pizzeria.model.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "usuarios")
@EqualsAndHashCode(callSuper = true)
public class Usuario extends PanacheEntity {
    
    @Column(nullable = false)
    private String nomeCompleto;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String senha;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Perfil perfil;
    
    @Column(nullable = false)
    private boolean ativo = true;
    
    public enum Perfil {
        ADMIN,
        ATENDENTE,
        CLIENTE
    }
} 