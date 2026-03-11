package com.alura.forumhub.repository;

import com.alura.forumhub.entity.Resposta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RespostaRepository extends JpaRepository<Resposta, Long> {

    Page<Resposta> findAllByTopicoIdAndAtivoTrue(Long topicoId, Pageable pageable);

    Optional<Resposta> findByIdAndAtivoTrue(Long id);
}
