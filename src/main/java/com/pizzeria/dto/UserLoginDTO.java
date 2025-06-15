package com.pizzeria.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserLoginDTO {
    @NotBlank
    @Email
    public String email;

    @NotBlank
    public String password;
} 