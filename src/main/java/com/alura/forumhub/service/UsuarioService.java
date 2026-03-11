package com.alura.forumhub.service;

import com.alura.forumhub.dto.UsuarioDTO.*;
import com.alura.forumhub.entity.Usuario;
import com.alura.forumhub.exception.BusinessException;
import com.alura.forumhub.exception.ResourceNotFoundException;
import com.alura.forumhub.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder   = passwordEncoder;
    }

    @Transactional
    public DetalhesUsuario cadastrar(CadastrarUsuario dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new BusinessException("E-mail já cadastrado: " + dto.email());
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .build();

        return new DetalhesUsuario(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public DetalhesUsuario detalhar(Long id) {
        return new DetalhesUsuario(buscar(id));
    }

    @Transactional
    public DetalhesUsuario atualizar(Long id, AtualizarUsuario dto, Usuario solicitante) {
        if (!solicitante.getId().equals(id)) {
            throw new BusinessException("Você não tem permissão para editar este usuário.");
        }
        Usuario usuario = buscar(id);
        if (dto.nome()  != null && !dto.nome().isBlank())  usuario.setNome(dto.nome());
        if (dto.senha() != null && !dto.senha().isBlank()) usuario.setSenha(passwordEncoder.encode(dto.senha()));
        return new DetalhesUsuario(usuario);
    }

    @Transactional
    public void excluir(Long id, Usuario solicitante) {
        if (!solicitante.getId().equals(id)) {
            throw new BusinessException("Você não tem permissão para excluir este usuário.");
        }
        Usuario usuario = buscar(id);
        usuario.setAtivo(false);
    }

    private Usuario buscar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));
    }
}
