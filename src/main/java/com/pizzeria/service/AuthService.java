package com.pizzeria.service;

import com.pizzeria.dto.AuthDTO;
import com.pizzeria.dto.AuthResponseDTO;
import com.pizzeria.model.entity.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Set;

@ApplicationScoped
public class AuthService {
    @Inject
    TokenService tokenService;
    @Inject
    CookieService cookieService;
    @Transactional
    public AuthResponseDTO authenticate(AuthDTO authDTO) {
        Usuario user;

        try {
            user = Usuario.find("email", authDTO.getEmail()).firstResult();
            if (!BCrypt.checkpw(authDTO.getSenha(), user.getSenha())) {
                throw new RuntimeException("Email ou senha inválidos");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        if (!user.isAtivo()) {
            throw new RuntimeException("Usuário inativo");
        }
        String token = tokenService.generateToken(user.getNomeCompleto(), user.getEmail(), "");

        return new AuthResponseDTO(
                token,
                user.getNomeCompleto(),
                user.getEmail(),
                user.getPerfil()
        );
    }
} 