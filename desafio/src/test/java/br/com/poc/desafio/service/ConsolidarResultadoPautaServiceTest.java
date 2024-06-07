package br.com.poc.desafio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.poc.desafio.domain.entity.PautaEntity;
import br.com.poc.desafio.domain.type.VotoType;
import br.com.poc.desafio.exception.core.BusinessException;
import br.com.poc.desafio.repository.PautaRepository;
import br.com.poc.desafio.repository.VotoAssociadoRepository;
import br.com.poc.desafio.stubs.PautaEntityStub;

@ExtendWith(MockitoExtension.class)
class ConsolidarResultadoPautaServiceTest {

    @InjectMocks
    private ConsolidarResultadoPautaService service;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private VotoAssociadoRepository votoRepository;

    private final Random random = new Random();

    @Test
    void deveConsolidarVotosComSucesso() {
        final Long pautaId = random.nextLong();
        final PautaEntity pautaEntity = PautaEntityStub.generate().build();
        final var quantidadeVotosSim = random.nextInt();
        final var quantidadeVotosNao = random.nextInt();

        when(pautaRepository.findById(any())).thenReturn(Optional.ofNullable(pautaEntity));
        when(votoRepository.countBySessaoVotacao_PautaIdAndVoto(any(), eq(VotoType.SIM))).thenReturn(
            quantidadeVotosSim);
        when(votoRepository.countBySessaoVotacao_PautaIdAndVoto(any(), eq(VotoType.NAO))).thenReturn(
            quantidadeVotosNao);

        final var atual = service.consolidar(pautaId);

        assertNotNull(atual);
        assertEquals(pautaEntity.getTitulo(), atual.titulo());
        assertEquals(pautaEntity.getDataCriacao(), atual.dataCriacao());
        assertEquals(quantidadeVotosSim, atual.quantidadeVotosSim());
        assertEquals(quantidadeVotosNao, atual.quantidadeVotosNao());

        verify(pautaRepository).findById(pautaId);
        verify(votoRepository).countBySessaoVotacao_PautaIdAndVoto(pautaId, VotoType.SIM);
        verify(votoRepository).countBySessaoVotacao_PautaIdAndVoto(pautaId, VotoType.NAO);
    }

    @Test
    void deveFalharQuandoPautaNaoExistir() {
        final Long pautaId = random.nextLong();

        when(pautaRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.consolidar(pautaId));

        verify(pautaRepository).findById(pautaId);
        verifyNoInteractions(votoRepository);
    }
}