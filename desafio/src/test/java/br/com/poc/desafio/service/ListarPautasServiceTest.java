package br.com.poc.desafio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.poc.desafio.repository.PautaRepository;
import br.com.poc.desafio.stubs.PautaEntityStub;

@ExtendWith(MockitoExtension.class)
class ListarPautasServiceTest {

    @InjectMocks
    private ListarPautasService service;

    @Mock
    private PautaRepository repository;

    @Test
    void deveListarPautasComSucesso() {
        final var pautaEntity = PautaEntityStub.generate().build();

        when(repository.findAll()).thenReturn(Collections.singletonList(pautaEntity));

        final var atual = service.listar();
        final var pautaResponse = atual.getFirst();

        assertNotNull(atual);
        assertEquals(1, atual.size());
        assertEquals(pautaEntity.getId(), pautaResponse.id());
        assertEquals(pautaEntity.getTitulo(), pautaResponse.titulo());
        assertEquals(pautaEntity.getDataCriacao(), pautaResponse.dataCriacao());

        verify(repository).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverPautas() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        final var atual = service.listar();

        assertNotNull(atual);
        assertEquals(0, atual.size());

        verify(repository).findAll();
    }
}