package com.alura.forumhub;

import com.alura.forumhub.dto.TopicoDTO.*;
import com.alura.forumhub.entity.StatusTopico;
import com.alura.forumhub.entity.Topico;
import com.alura.forumhub.entity.Usuario;
import com.alura.forumhub.exception.DuplicateTopicException;
import com.alura.forumhub.exception.ResourceNotFoundException;
import com.alura.forumhub.repository.TopicoRepository;
import com.alura.forumhub.service.TopicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopicoServiceTest {

    @Mock
    private TopicoRepository topicoRepository;

    @InjectMocks
    private TopicoService topicoService;

    private Usuario autorMock;

    @BeforeEach
    void setUp() {
        autorMock = Usuario.builder()
                .id(1L)
                .nome("Autor Teste")
                .email("autor@teste.com")
                .senha("senha-hash")
                .build();
    }

    @Test
    @DisplayName("Deve cadastrar tópico com sucesso quando não existe duplicata")
    void deveCadastrarTopico() {
        var dto    = new CadastrarTopico("Título válido", "Mensagem com mais de dez chars", "Spring Boot");
        var topico = Topico.builder()
                .id(1L).titulo(dto.titulo()).mensagem(dto.mensagem())
                .curso(dto.curso()).autor(autorMock).build();

        when(topicoRepository.existsByTituloAndMensagemAndAtivoTrue(dto.titulo(), dto.mensagem()))
                .thenReturn(false);
        when(topicoRepository.save(any(Topico.class))).thenReturn(topico);

        DetalhesTopico resultado = topicoService.cadastrar(dto, autorMock);

        assertThat(resultado.titulo()).isEqualTo(dto.titulo());
        assertThat(resultado.autor()).isEqualTo(autorMock.getNome());
        verify(topicoRepository).save(any(Topico.class));
    }

    @Test
    @DisplayName("Deve lançar DuplicateTopicException ao cadastrar tópico duplicado")
    void deveLancarExcecaoDuplicata() {
        var dto = new CadastrarTopico("Título duplicado", "Mensagem duplicada aqui", "Java");

        when(topicoRepository.existsByTituloAndMensagemAndAtivoTrue(dto.titulo(), dto.mensagem()))
                .thenReturn(true);

        assertThatThrownBy(() -> topicoService.cadastrar(dto, autorMock))
                .isInstanceOf(DuplicateTopicException.class)
                .hasMessageContaining("duplicado");

        verify(topicoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao detalhar tópico inexistente")
    void deveLancarExcecaoTopicoNaoEncontrado() {
        when(topicoRepository.findByIdAndAtivoTrue(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> topicoService.detalhar(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Deve excluir tópico (soft-delete)")
    void deveExcluirTopico() {
        var topico = Topico.builder()
                .id(1L).titulo("T").mensagem("M válida aqui").curso("C")
                .autor(autorMock).ativo(true).build();

        when(topicoRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(topico));

        topicoService.excluir(1L);

        assertThat(topico.getAtivo()).isFalse();
    }

    @Test
    @DisplayName("Deve atualizar apenas os campos informados")
    void deveAtualizarTopicoParcialmente() {
        var topico = Topico.builder()
                .id(1L).titulo("Título original").mensagem("Mensagem original valida")
                .curso("Curso original").status(StatusTopico.ABERTO)
                .autor(autorMock).ativo(true).build();

        when(topicoRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(topico));
        when(topicoRepository.existsByTituloAndMensagemAndAtivoTrue(any(), any())).thenReturn(false);

        var dto = new AtualizarTopico("Título novo", null, null, StatusTopico.SOLUCIONADO);
        DetalhesTopico resultado = topicoService.atualizar(1L, dto, autorMock);

        assertThat(resultado.titulo()).isEqualTo("Título novo");
        assertThat(resultado.status()).isEqualTo(StatusTopico.SOLUCIONADO);
        assertThat(resultado.mensagem()).isEqualTo("Mensagem original valida");
    }
}
