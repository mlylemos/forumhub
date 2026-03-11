package com.alura.forumhub.repository;

import com.alura.forumhub.entity.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    // Busca apenas tópicos ativos
    Page<Topico> findAllByAtivoTrue(Pageable pageable);

    // Verificação de duplicidade (mesmo título e mesma mensagem, ativo)
    boolean existsByTituloAndMensagemAndAtivoTrue(String titulo, String mensagem);

    // Busca por curso e/ou ano de criação
    @Query("""
            SELECT t FROM Topico t
            WHERE t.ativo = true
              AND (:curso IS NULL OR LOWER(t.curso) LIKE LOWER(CONCAT('%', :curso, '%')))
              AND (:ano   IS NULL OR YEAR(t.criadoEm) = :ano)
            """)
    Page<Topico> buscarPorCursoEAno(@Param("curso") String curso,
                                     @Param("ano")   Integer ano,
                                     Pageable pageable);

    // Busca tópico ativo por ID
    Optional<Topico> findByIdAndAtivoTrue(Long id);
}
