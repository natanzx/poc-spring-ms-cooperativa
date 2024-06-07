package br.com.poc.desafio.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.poc.desafio.domain.entity.PautaEntity;
import br.com.poc.desafio.domain.request.PautaRequest;
import br.com.poc.desafio.repository.PautaRepository;
import br.com.poc.desafio.stubs.PautaEntityStub;

@ExtendWith(MockitoExtension.class)
class CadastrarPautaServiceTest {

    @InjectMocks
    private CadastrarPautaService service;

    @Mock
    private PautaRepository repository;

    @Captor
    private ArgumentCaptor<PautaEntity> captor;

    @Test
    void deveCadastrarComSucesso() {
        final var request = new PautaRequest(randomAlphabetic(10));
        final var pautaEntity = PautaEntityStub.generate().titulo(request.titulo()).build();

        when(repository.save(any())).thenReturn(pautaEntity);

        final var atual = service.cadastrar(request);

        verify(repository).save(captor.capture());

        final var captorValue = captor.getValue();
        assertEquals(captorValue.getTitulo(), atual.titulo());
        assertEquals(pautaEntity.getId(), atual.id());
        assertEquals(pautaEntity.getDataCriacao(), atual.dataCriacao());
    }
}