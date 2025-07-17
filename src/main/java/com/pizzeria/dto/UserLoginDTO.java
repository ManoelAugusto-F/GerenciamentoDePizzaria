package com.pizzeria.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserLoginDTO {

    @Email
    public String email;


    public String password;
    public String roles;
} 