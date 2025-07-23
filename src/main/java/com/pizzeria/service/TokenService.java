package com.pizzeria.service;


import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;

@ApplicationScoped
public class TokenService {
    public String generateToken(String id, String roles) {
        Set<String> groups = new HashSet<>();
        groups.add(roles);
        return Jwt.issuer("http://localhost:8080")
                .upn(id)
                .groups(groups)
                .expiresIn(3600).sign();
    }
}
