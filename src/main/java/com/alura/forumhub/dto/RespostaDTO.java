package com.alura.forumhub.dto;

import com.alura.forumhub.entity.Resposta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class RespostaDTO {

    public record CadastrarResposta(

            @NotBlank(message = "Mensagem é obrigatória")
            @Size(min = 10, message = "Mensagem deve ter no mínimo 10 caracteres")
            String mensagem

    ) {}

    public record AtualizarResposta(

            @Size(min = 10)
            String mensagem,

            Boolean solucao

    ) {}

    public record DetalhesResposta(
            Long          id,
            String        mensagem,
            LocalDateTime criadoEm,
            String        autor,
            Boolean       solucao
    ) {
        public DetalhesResposta(Resposta r) {
            this(r.getId(), r.getMensagem(), r.getCriadoEm(), r.getAutor().getNome(), r.getSolucao());
        }
    }
}
