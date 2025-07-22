package com.pizzeria.service;

import com.pizzeria.model.entity.User;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
@ApplicationScoped
public class AuthService {
    @Inject
    UserService userService;
    @Context
    SecurityContext securityContext;
    public User AutenticateUser(){
        var principal = (DefaultJWTCallerPrincipal) securityContext.getUserPrincipal();
        if (principal == null) {
            throw new WebApplicationException("Usuário não autenticado", 401);
        }
        String email = principal.getClaim("upn"); // Obtém o email do usuário autenticado
        if (email == null || email.isEmpty()) {
            throw new WebApplicationException("Email do usuário não encontrado", 401);
        }
        return userService.findByEmail(email);
    }
}
