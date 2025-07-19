package com.pizzeria.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class TokenService {

    private Algorithm algorithm;

    public TokenService() {
        this.algorithm = loadAlgorithm();
    }

    private Algorithm loadAlgorithm() {
        try (InputStream is = TokenService.class.getResourceAsStream("/META-INF/resources/privateKey.pem")) {
            if (is == null) {
                throw new RuntimeException("Arquivo privateKey.pem não encontrado no classpath!");
            }

            String privateKeyPEM = new String(is.readAllBytes())
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            RSAPrivateKey privateKey = (RSAPrivateKey) kf.generatePrivate(spec);

            return Algorithm.RSA256(null, privateKey);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar chave privada para JWT", e);
        }
    }

    public String generateToken(String username, String email, String roles) {
        return JWT.create()
                .withIssuer("http://localhost:8080")
                .withClaim("email", email)
                .withClaim("groups", List.of(roles))
                .withExpiresAt(Date.from(LocalDateTime.now()
                        .plusHours(1)
                        .toInstant(ZoneOffset.of("-03:00"))))
                .sign(this.algorithm);
    }
}
