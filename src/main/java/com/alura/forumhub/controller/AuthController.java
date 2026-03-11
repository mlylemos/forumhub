package com.alura.forumhub.controller;

import com.alura.forumhub.dto.DadosAutenticacao;
import com.alura.forumhub.dto.DadosTokenJWT;
import com.alura.forumhub.entity.Usuario;
import com.alura.forumhub.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Tag(name = "Autenticação", description = "Endpoint para autenticação de usuários")
public class AuthController {

    private final AuthenticationManager authManager;
    private final TokenService          tokenService;

    public AuthController(AuthenticationManager authManager, TokenService tokenService) {
        this.authManager  = authManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    @Operation(summary = "Realizar login", description = "Autentica o usuário e retorna um token JWT Bearer.")
    public ResponseEntity<DadosTokenJWT> login(@RequestBody @Valid DadosAutenticacao dados) {
        var authToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var auth      = authManager.authenticate(authToken);
        var usuario   = (Usuario) auth.getPrincipal();
        var token     = tokenService.gerarToken(usuario);

        return ResponseEntity.ok(DadosTokenJWT.bearer(token, tokenService.getExpiracaoEm()));
    }
}
