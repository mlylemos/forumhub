package com.alura.forumhub.controller;

import com.alura.forumhub.dto.UsuarioDTO.*;
import com.alura.forumhub.entity.Usuario;
import com.alura.forumhub.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar usuário", description = "Cria uma nova conta de usuário (endpoint público).")
    public ResponseEntity<DetalhesUsuario> cadastrar(
            @RequestBody @Valid CadastrarUsuario dto,
            UriComponentsBuilder uriBuilder) {

        DetalhesUsuario usuario = usuarioService.cadastrar(dto);
        URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.id()).toUri();
        return ResponseEntity.created(uri).body(usuario);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Detalhar usuário")
    public ResponseEntity<DetalhesUsuario> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.detalhar(id));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Atualizar usuário", description = "O usuário só pode editar a si mesmo.")
    public ResponseEntity<DetalhesUsuario> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarUsuario dto,
            @AuthenticationPrincipal Usuario solicitante) {

        return ResponseEntity.ok(usuarioService.atualizar(id, dto, solicitante));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Excluir usuário", description = "Soft-delete — o usuário só pode excluir a si mesmo.")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario solicitante) {

        usuarioService.excluir(id, solicitante);
        return ResponseEntity.noContent().build();
    }
}
