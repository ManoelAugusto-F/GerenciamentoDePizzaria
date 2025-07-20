package com.pizzeria.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;

import java.time.Duration;

@ApplicationScoped
public class
CookieService {
    private static final String COOKIE_NAME = "token";

    public NewCookie generateJwtCookie(String jwtToken) {
        return new NewCookie(
                COOKIE_NAME,                      // nome do cookie
                jwtToken,                     // valor do cookie
                "/",                          // path
                null,                        // domínio (null = mesmo domínio)
                NewCookie.DEFAULT_VERSION,    // versão
                "JWT Auth Token",             // comentário
                (int) Duration.ofHours(1).getSeconds(), // maxAge (em segundos)
                true                         // httpOnly
        );
    }

    public String getTokenFromCookie(Cookie cookie) {
        return cookie != null ? cookie.getValue() : null;
    }
}

