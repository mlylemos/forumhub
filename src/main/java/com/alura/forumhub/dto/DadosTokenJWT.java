package com.alura.forumhub.dto;

public record DadosTokenJWT(String token, String tipo, long expiracaoEm) {

    public static DadosTokenJWT bearer(String token, long expiracaoEm) {
        return new DadosTokenJWT(token, "Bearer", expiracaoEm);
    }
}
