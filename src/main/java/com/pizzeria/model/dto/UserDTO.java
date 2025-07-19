package com.pizzeria.model.dto;

import jakarta.validation.constraints.Email;

public class UserDTO {
    private Long id;
    @Email
    public String email;
    public String name;
    public String password;
    public String roles;
    public Boolean ativo;
} 