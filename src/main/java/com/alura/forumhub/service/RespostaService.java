package com.alura.forumhub.service;

import com.alura.forumhub.dto.RespostaDTO.*;
import com.alura.forumhub.entity.Resposta;
import com.alura.forumhub.entity.Topico;
import com.alura.forumhub.entity.Usuario;
import com.alura.forumhub.exception.BusinessException;
import com.alura.forumhub.exception.ResourceNotFoundException;
import com.alura.forumhub.repository.RespostaRepository;
import com.alura.forumhub.repository.TopicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RespostaService {

    private final RespostaRepository respostaRepository;
    private final TopicoRepository   topicoRepository;

    public RespostaService(RespostaRepository respostaRepository, TopicoRepository topicoRepository) {
        this.respostaRepository = respostaRepository;
        this.topicoRepository   = topicoRepository;
    }

    @Transactional
    public DetalhesResposta cadastrar(Long topicoId, CadastrarResposta dto, Usuario autor) {
        Topico topico = topicoRepository.findByIdAndAtivoTrue(topicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Tópico não encontrado: " + topicoId));

        Resposta resposta = Resposta.builder()
                .mensagem(dto.mensagem())
                .topico(topico)
                .autor(autor)
                .build();

        return new DetalhesResposta(respostaRepository.save(resposta));
    }

    @Transactional(readOnly = true)
    public Page<DetalhesResposta> listar(Long topicoId, Pageable pageable) {
        return respostaRepository.findAllByTopicoIdAndAtivoTrue(topicoId, pageable)
                .map(DetalhesResposta::new);
    }

    @Transactional
    public DetalhesResposta atualizar(Long id, AtualizarResposta dto, Usuario solicitante) {
        Resposta resposta = buscarAtiva(id);
        if (!resposta.getAutor().getId().equals(solicitante.getId())) {
            throw new BusinessException("Você não tem permissão para editar esta resposta.");
        }
        if (dto.mensagem() != null && !dto.mensagem().isBlank()) resposta.setMensagem(dto.mensagem());
        if (dto.solucao()  != null) resposta.setSolucao(dto.solucao());
        return new DetalhesResposta(resposta);
    }

    @Transactional
    public void excluir(Long id, Usuario solicitante) {
        Resposta resposta = buscarAtiva(id);
        if (!resposta.getAutor().getId().equals(solicitante.getId())) {
            throw new BusinessException("Você não tem permissão para excluir esta resposta.");
        }
        resposta.setAtivo(false);
    }

    private Resposta buscarAtiva(Long id) {
        return respostaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resposta não encontrada com id: " + id));
    }
}
