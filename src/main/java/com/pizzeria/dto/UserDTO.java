package com.pizzeria.dto;

import com.pizzeria.model.entity.Usuario.Perfil;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String nomeCompleto;
    private String email;
    private Perfil perfil;
    private boolean ativo;
} 