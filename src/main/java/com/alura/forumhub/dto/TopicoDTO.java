package com.alura.forumhub.dto;

import com.alura.forumhub.entity.StatusTopico;
import com.alura.forumhub.entity.Topico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

// ── Cadastro ───────────────────────────────────────────────────────────────────

public class TopicoDTO {

    public record CadastrarTopico(

            @NotBlank(message = "Título é obrigatório")
            @Size(min = 5, max = 200, message = "Título deve ter entre 5 e 200 caracteres")
            String titulo,

            @NotBlank(message = "Mensagem é obrigatória")
            @Size(min = 10, message = "Mensagem deve ter no mínimo 10 caracteres")
            String mensagem,

            @NotBlank(message = "Curso é obrigatório")
            @Size(min = 2, max = 150, message = "Curso deve ter entre 2 e 150 caracteres")
            String curso

    ) {}

    // ── Atualização ─────────────────────────────────────────────────────────────

    public record AtualizarTopico(

            @Size(min = 5, max = 200, message = "Título deve ter entre 5 e 200 caracteres")
            String titulo,

            @Size(min = 10, message = "Mensagem deve ter no mínimo 10 caracteres")
            String mensagem,

            @Size(min = 2, max = 150, message = "Curso deve ter entre 2 e 150 caracteres")
            String curso,

            StatusTopico status

    ) {}

    // ── Resposta completa ───────────────────────────────────────────────────────

    public record DetalhesTopico(
            Long           id,
            String         titulo,
            String         mensagem,
            LocalDateTime  criadoEm,
            StatusTopico   status,
            String         autor,
            String         curso
    ) {
        public DetalhesTopico(Topico t) {
            this(
                t.getId(),
                t.getTitulo(),
                t.getMensagem(),
                t.getCriadoEm(),
                t.getStatus(),
                t.getAutor().getNome(),
                t.getCurso()
            );
        }
    }

    // ── Resposta resumida (listagem) ────────────────────────────────────────────

    public record ResumoTopico(
            Long          id,
            String        titulo,
            LocalDateTime criadoEm,
            StatusTopico  status,
            String        autor,
            String        curso
    ) {
        public ResumoTopico(Topico t) {
            this(
                t.getId(),
                t.getTitulo(),
                t.getCriadoEm(),
                t.getStatus(),
                t.getAutor().getNome(),
                t.getCurso()
            );
        }
    }
}
