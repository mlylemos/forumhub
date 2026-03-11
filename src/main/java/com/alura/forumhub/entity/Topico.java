package com.alura.forumhub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "topicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensagem;

    @Column(name = "criado_em", nullable = false)
    @Builder.Default
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusTopico status = StatusTopico.ABERTO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @Column(nullable = false, length = 150)
    private String curso;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    // ── Domain methods ─────────────────────────────────────────────────────────

    public void atualizar(String titulo, String mensagem, String curso, StatusTopico status) {
        if (titulo   != null && !titulo.isBlank())   this.titulo   = titulo;
        if (mensagem != null && !mensagem.isBlank())  this.mensagem = mensagem;
        if (curso    != null && !curso.isBlank())     this.curso    = curso;
        if (status   != null)                          this.status   = status;
    }

    public void excluir() {
        this.ativo = false;
    }
}
