package com.pizzeria.dto;

import com.pizzeria.model.entity.Usuario.Perfil;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String nomeCompleto;
    private String email;
    private Perfil perfil;
} 