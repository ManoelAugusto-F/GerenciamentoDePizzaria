package com.pizzeria.model.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    @Email
    private String email;
    private String name;
    private String password;
    private String roles;
    private Boolean ativo;
} 