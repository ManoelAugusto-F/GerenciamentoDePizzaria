package com.pizzeria.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegisterDTO {
    @NotBlank
    @Size(min = 2, max = 100)
    public String name;

    @NotBlank
    @Email
    public String email;

    @NotBlank
    @Size(min = 8)
    public String password;
} 