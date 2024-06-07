package br.com.poc.desafio.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.poc.desafio.repository.VotoAssociadoRepository;

@ExtendWith(MockitoExtension.class)
class ValidarAssociadoVotouEmPautaServiceTest {

    @InjectMocks
    private ValidarAssociadoVotouEmPautaService service;

    @Mock
    private VotoAssociadoRepository repository;

    private final Random random = new Random();

    @Test
    public void deveValidarAssociadoJaVotou() {
        final Long pautaId = random.nextLong();
        final String associadoCpf = RandomStringUtils.randomNumeric(11);

        when(repository.existsBySessaoVotacao_PautaIdAndAssociadoCpf(any(), any())).thenReturn(true);

        final Boolean resultado = service.validar(pautaId, associadoCpf);

        assertTrue(resultado);
        verify(repository).existsBySessaoVotacao_PautaIdAndAssociadoCpf(pautaId, associadoCpf);
    }

}