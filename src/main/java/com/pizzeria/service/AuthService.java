package com.pizzeria.service;

import com.pizzeria.dto.AuthDTO;
import com.pizzeria.dto.AuthResponseDTO;
import com.pizzeria.model.entity.Usuario;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class AuthService {

    @Transactional
    public AuthResponseDTO authenticate(AuthDTO authDTO) {
        Usuario usuario = Usuario.find("email", authDTO.getEmail()).firstResult();
        
        if (usuario == null || !BCrypt.checkpw(authDTO.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Credenciais inválidas");
        }
        
        if (!usuario.isAtivo()) {
            throw new RuntimeException("Usuário inativo");
        }
        
        String token = generateToken(usuario);
        
        return new AuthResponseDTO(
            token,
            usuario.getNomeCompleto(),
            usuario.getEmail(),
            usuario.getPerfil()
        );
    }
    
    private String generateToken(Usuario usuario) {
        return Jwt.issuer("https://pizzeria.com")
                 .subject(usuario.getEmail())
                 .groups(new HashSet<>(Arrays.asList(usuario.getPerfil().name())))
                 .expiresIn(Duration.ofHours(24))
                 .sign();
    }
} 