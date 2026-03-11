package com.alura.forumhub.controller;

import com.alura.forumhub.dto.TopicoDTO.*;
import com.alura.forumhub.entity.Usuario;
import com.alura.forumhub.service.TopicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Tópicos", description = "CRUD completo de tópicos do fórum")
public class TopicoController {

    private final TopicoService topicoService;

    public TopicoController(TopicoService topicoService) {
        this.topicoService = topicoService;
    }

    // ── POST /topicos ──────────────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Cadastrar tópico", description = "Cria um novo tópico associado ao usuário autenticado.")
    public ResponseEntity<DetalhesTopico> cadastrar(
            @RequestBody @Valid CadastrarTopico dto,
            @AuthenticationPrincipal Usuario autor,
            UriComponentsBuilder uriBuilder) {

        DetalhesTopico resposta = topicoService.cadastrar(dto, autor);
        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(resposta.id()).toUri();
        return ResponseEntity.created(uri).body(resposta);
    }

    // ── GET /topicos ───────────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "Listar tópicos",
               description = "Retorna todos os tópicos ativos. Filtre por 'curso' e/ou 'ano' como query params.")
    public ResponseEntity<Page<ResumoTopico>> listar(
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) Integer ano,
            @PageableDefault(size = 10, sort = "criadoEm", direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.ok(topicoService.listar(curso, ano, pageable));
    }

    // ── GET /topicos/{id} ──────────────────────────────────────────────────────

    @GetMapping("/{id}")
    @Operation(summary = "Detalhar tópico", description = "Retorna os detalhes completos de um tópico pelo seu ID.")
    public ResponseEntity<DetalhesTopico> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(topicoService.detalhar(id));
    }

    // ── PUT /topicos/{id} ──────────────────────────────────────────────────────

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tópico", description = "Atualiza os campos de um tópico existente.")
    public ResponseEntity<DetalhesTopico> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarTopico dto,
            @AuthenticationPrincipal Usuario solicitante) {

        return ResponseEntity.ok(topicoService.atualizar(id, dto, solicitante));
    }

    // ── DELETE /topicos/{id} ───────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir tópico", description = "Realiza soft-delete de um tópico (ativo = false).")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        topicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
