package com.pizzeria.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@ApplicationScoped
public class TokenService {

    private static final String SECRET = "secreta"; // substitua por algo mais seguro depois
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    public String generateToken(String username, String email, String roles) {
        return JWT.create()
                .withIssuer("https://pizzeria.com")
                .withSubject(username)
                .withClaim("email", email)
                .withClaim("roles", roles)
                .withExpiresAt(Date.from(LocalDateTime.now()
                        .plusHours(1)
                        .toInstant(ZoneOffset.of("-03:00"))))
                .sign(ALGORITHM);
    }
}