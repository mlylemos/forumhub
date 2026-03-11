package com.alura.forumhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosAutenticacao(
        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        String senha
) {}
