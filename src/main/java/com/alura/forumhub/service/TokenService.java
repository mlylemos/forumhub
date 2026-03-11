package com.alura.forumhub.service;

import com.alura.forumhub.entity.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private static final String ISSUER = "ForumHub-API";

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expiration}")
    private long expirationMs;

    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getEmail())
                    .withClaim("id", usuario.getId())
                    .withClaim("nome", usuario.getNome())
                    .withExpiresAt(dataExpiracao())
                    .sign(algorithm);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token JWT inválido ou expirado: " + e.getMessage());
        }
    }

    public Instant dataExpiracao() {
        return LocalDateTime.now()
                .plusSeconds(expirationMs / 1000)
                .toInstant(ZoneOffset.of("-03:00"));
    }

    public long getExpiracaoEm() {
        return expirationMs;
    }
}
