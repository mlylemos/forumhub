package com.alura.forumhub.service;

import com.alura.forumhub.dto.TopicoDTO.*;
import com.alura.forumhub.entity.Topico;
import com.alura.forumhub.entity.Usuario;
import com.alura.forumhub.exception.DuplicateTopicException;
import com.alura.forumhub.exception.ResourceNotFoundException;
import com.alura.forumhub.repository.TopicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TopicoService {

    private final TopicoRepository topicoRepository;

    public TopicoService(TopicoRepository topicoRepository) {
        this.topicoRepository = topicoRepository;
    }

    @Transactional
    public DetalhesTopico cadastrar(CadastrarTopico dto, Usuario autor) {
        validarDuplicidade(dto.titulo(), dto.mensagem());

        Topico topico = Topico.builder()
                .titulo(dto.titulo())
                .mensagem(dto.mensagem())
                .curso(dto.curso())
                .autor(autor)
                .build();

        return new DetalhesTopico(topicoRepository.save(topico));
    }

    @Transactional(readOnly = true)
    public Page<ResumoTopico> listar(String curso, Integer ano, Pageable pageable) {
        if (curso != null || ano != null) {
            return topicoRepository.buscarPorCursoEAno(curso, ano, pageable)
                    .map(ResumoTopico::new);
        }
        return topicoRepository.findAllByAtivoTrue(pageable).map(ResumoTopico::new);
    }

    @Transactional(readOnly = true)
    public DetalhesTopico detalhar(Long id) {
        return new DetalhesTopico(buscarAtivo(id));
    }

    @Transactional
    public DetalhesTopico atualizar(Long id, AtualizarTopico dto, Usuario solicitante) {
        Topico topico = buscarAtivo(id);

        // Verifica duplicidade apenas se título ou mensagem mudaram
        String novoTitulo   = dto.titulo()   != null ? dto.titulo()   : topico.getTitulo();
        String novaMensagem = dto.mensagem() != null ? dto.mensagem() : topico.getMensagem();
        boolean mudouConteudo =
                !novoTitulo.equals(topico.getTitulo()) || !novaMensagem.equals(topico.getMensagem());

        if (mudouConteudo) {
            validarDuplicidade(novoTitulo, novaMensagem);
        }

        topico.atualizar(dto.titulo(), dto.mensagem(), dto.curso(), dto.status());
        return new DetalhesTopico(topico);
    }

    @Transactional
    public void excluir(Long id) {
        Topico topico = buscarAtivo(id);
        topico.excluir();   // soft-delete
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private Topico buscarAtivo(Long id) {
        return topicoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tópico não encontrado com id: " + id));
    }

    private void validarDuplicidade(String titulo, String mensagem) {
        if (topicoRepository.existsByTituloAndMensagemAndAtivoTrue(titulo, mensagem)) {
            throw new DuplicateTopicException("Já existe um tópico com este título e mensagem.");
        }
    }
}
