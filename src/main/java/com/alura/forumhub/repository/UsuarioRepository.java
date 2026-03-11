package com.alura.forumhub.repository;

import com.alura.forumhub.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<UserDetails> findByEmail(String email);

    boolean existsByEmail(String email);
}
