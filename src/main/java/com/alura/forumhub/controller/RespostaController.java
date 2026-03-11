package com.alura.forumhub.controller;

import com.alura.forumhub.dto.RespostaDTO.*;
import com.alura.forumhub.entity.Usuario;
import com.alura.forumhub.service.RespostaService;
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
@RequestMapping("/topicos/{topicoId}/respostas")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Respostas", description = "Gerenciamento de respostas dos tópicos")
public class RespostaController {

    private final RespostaService respostaService;

    public RespostaController(RespostaService respostaService) {
        this.respostaService = respostaService;
    }

    @PostMapping
    @Operation(summary = "Responder tópico")
    public ResponseEntity<DetalhesResposta> cadastrar(
            @PathVariable Long topicoId,
            @RequestBody @Valid CadastrarResposta dto,
            @AuthenticationPrincipal Usuario autor,
            UriComponentsBuilder uriBuilder) {

        DetalhesResposta resposta = respostaService.cadastrar(topicoId, dto, autor);
        URI uri = uriBuilder
                .path("/topicos/{topicoId}/respostas/{id}")
                .buildAndExpand(topicoId, resposta.id())
                .toUri();
        return ResponseEntity.created(uri).body(resposta);
    }

    @GetMapping
    @Operation(summary = "Listar respostas do tópico")
    public ResponseEntity<Page<DetalhesResposta>> listar(
            @PathVariable Long topicoId,
            @PageableDefault(size = 10, sort = "criadoEm", direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.ok(respostaService.listar(topicoId, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar resposta", description = "Somente o autor da resposta pode editá-la.")
    public ResponseEntity<DetalhesResposta> atualizar(
            @PathVariable Long topicoId,
            @PathVariable Long id,
            @RequestBody @Valid AtualizarResposta dto,
            @AuthenticationPrincipal Usuario solicitante) {

        return ResponseEntity.ok(respostaService.atualizar(id, dto, solicitante));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir resposta", description = "Somente o autor da resposta pode excluí-la.")
    public ResponseEntity<Void> excluir(
            @PathVariable Long topicoId,
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario solicitante) {

        respostaService.excluir(id, solicitante);
        return ResponseEntity.noContent().build();
    }
}
