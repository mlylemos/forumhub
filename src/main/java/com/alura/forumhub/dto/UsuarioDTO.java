package com.alura.forumhub.dto;

import com.alura.forumhub.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class UsuarioDTO {

    public record CadastrarUsuario(

            @NotBlank(message = "Nome é obrigatório")
            @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
            String nome,

            @NotBlank(message = "E-mail é obrigatório")
            @Email(message = "Formato de e-mail inválido")
            String email,

            @NotBlank(message = "Senha é obrigatória")
            @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
            String senha

    ) {}

    public record AtualizarUsuario(

            @Size(min = 2, max = 100)
            String nome,

            @Size(min = 6)
            String senha

    ) {}

    public record DetalhesUsuario(
            Long          id,
            String        nome,
            String        email,
            LocalDateTime criadoEm
    ) {
        public DetalhesUsuario(Usuario u) {
            this(u.getId(), u.getNome(), u.getEmail(), u.getCriadoEm());
        }
    }
}
