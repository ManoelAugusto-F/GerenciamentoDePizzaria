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
        String token = "aqui vai catar o token dos cookies, verifica a lib que foi usada pra criar os cookies que deve ter como catar por lá";
        // verificar se os dados que estão em user batem com o token criado anteriormente no login que agora estão
        // no cookie apos login, não precisa do return aqui, faça algo como ele volta para o login caso os dados não baterem com os do cookie
        // , você tem que decodificar o token q está nos cookies antes, antes de iniciar esa nova função verifica se os cookies estão la.

        return new AuthResponseDTO(
                token,
                user.getNomeCompleto(),
                user.getEmail(),
                user.getPerfil()
        );


    }
}