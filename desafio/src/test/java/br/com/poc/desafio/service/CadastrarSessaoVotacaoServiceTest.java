package br.com.poc.desafio.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.poc.desafio.domain.entity.PautaEntity;
import br.com.poc.desafio.domain.entity.SessaoVotacaoEntity;
import br.com.poc.desafio.domain.request.SessaoVotacaoRequest;
import br.com.poc.desafio.exception.core.BusinessException;
import br.com.poc.desafio.repository.PautaRepository;
import br.com.poc.desafio.repository.SessaoVotacaoRepository;
import br.com.poc.desafio.stubs.PautaEntityStub;

@ExtendWith(MockitoExtension.class)
class CadastrarSessaoVotacaoServiceTest {

    @InjectMocks
    private CadastrarSessaoVotacaoService service;

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private BuscarSessaoVotacaoAbertaService buscarSessaoVotacaoAbertaService;

    @Captor
    private ArgumentCaptor<SessaoVotacaoEntity> captor;

    private final Random random = new Random();

    @Test
    void deveCadastrarSessaoComSucesso() {
        final Long pautaId = random.nextLong();
        final PautaEntity pautaEntity = PautaEntityStub.generate().build();

        when(pautaRepository.findById(any())).thenReturn(Optional.ofNullable(pautaEntity));
        when(buscarSessaoVotacaoAbertaService.buscar(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> service.cadastrar(pautaId, new SessaoVotacaoRequest(2)));

        verify(pautaRepository).findById(pautaId);
        verify(buscarSessaoVotacaoAbertaService).buscar(pautaId);
        verify(sessaoVotacaoRepository).save(captor.capture());

        final var captorValue = captor.getValue();
        assertEquals(pautaEntity, captorValue.getPauta());
        assertNotNull(captorValue.getDataInicio());
        assertNotNull(captorValue.getDataFim());
    }

    @Test
    void deveNaoCadastrarSessaoQuandoPautaInexistente() {
        final Long pautaId = random.nextLong();

        when(pautaRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.cadastrar(pautaId, new SessaoVotacaoRequest(2)));

        verify(pautaRepository).findById(pautaId);
        verifyNoInteractions(buscarSessaoVotacaoAbertaService, sessaoVotacaoRepository);
    }

    @Test
    void deveNaoCadastrarSessaoQuandoSessaoJaAberta() {
        final Long pautaId = random.nextLong();
        final PautaEntity pautaEntity = PautaEntityStub.generate().build();
        final var sessaoVotacaoEntity = SessaoVotacaoEntity.builder().build();

        when(pautaRepository.findById(any())).thenReturn(Optional.ofNullable(pautaEntity));
        when(buscarSessaoVotacaoAbertaService.buscar(any())).thenReturn(Optional.of(sessaoVotacaoEntity));

        assertThrows(BusinessException.class, () -> service.cadastrar(pautaId, new SessaoVotacaoRequest(2)));

        verify(pautaRepository).findById(pautaId);
        verify(buscarSessaoVotacaoAbertaService).buscar(pautaId);
        verifyNoInteractions(sessaoVotacaoRepository);
    }
}