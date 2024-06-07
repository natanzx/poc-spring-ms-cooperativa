package br.com.poc.desafio.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.poc.desafio.domain.entity.SessaoVotacaoEntity;
import br.com.poc.desafio.repository.SessaoVotacaoRepository;

@ExtendWith(MockitoExtension.class)
class BuscarSessaoVotacaoAbertaServiceTest {

    @InjectMocks
    private BuscarSessaoVotacaoAbertaService service;

    @Mock
    private SessaoVotacaoRepository repository;

    private final Random random = new Random();

    @Test
    void deveBuscarSessaoVotacaoAbertaComSucesso() {
        final Long pautaId = random.nextLong();
        final var sessaoVotacaoEntity = SessaoVotacaoEntity.builder().build();

        when(repository.findByPautaIdAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(any(), any(), any()))
            .thenReturn(Optional.of(sessaoVotacaoEntity));

        final var atual = service.buscar(pautaId);

        assertNotNull(atual);

        verify(repository)
            .findByPautaIdAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(eq(pautaId), any(LocalDateTime.class),
                any(LocalDateTime.class));
    }
}