package br.com.poc.desafio.service;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

import br.com.poc.desafio.client.UserFeign;
import br.com.poc.desafio.client.response.ValidarUserResponse;
import br.com.poc.desafio.client.response.ValidarUserResponse.StatusVotacao;
import br.com.poc.desafio.domain.entity.SessaoVotacaoEntity;
import br.com.poc.desafio.domain.entity.VotoAssociadoEntity;
import br.com.poc.desafio.domain.request.VotoAssociadoRequest;
import br.com.poc.desafio.domain.type.VotoType;
import br.com.poc.desafio.exception.core.BusinessException;
import br.com.poc.desafio.repository.PautaRepository;
import br.com.poc.desafio.repository.VotoAssociadoRepository;
import br.com.poc.desafio.stubs.PautaEntityStub;
import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class CadastrarVotoAssociadoServiceTest {

    @InjectMocks
    private CadastrarVotoAssociadoService service;

    @Mock
    private VotoAssociadoRepository votoAssociadoRepository;

    @Mock
    private BuscarSessaoVotacaoAbertaService buscarSessaoVotacaoAbertaService;

    @Mock
    private ValidarAssociadoVotouEmPautaService validarAssociadoVotouEmPautaService;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private UserFeign userFeign;

    @Captor
    private ArgumentCaptor<VotoAssociadoEntity> captor;

    private final Random random = new Random();

    private final VotoAssociadoRequest request = new VotoAssociadoRequest(randomNumeric(11), VotoType.SIM);

    @Test
    void deveCadastrarVotoComSucesso() {
        final Long pautaId = random.nextLong();
        final var pautaEntity = PautaEntityStub.generate().build();
        final var validarUserResponse = ValidarUserResponse.builder().build();
        final var sessaoVotacaoEntity = SessaoVotacaoEntity.builder().build();

        when(userFeign.validarUser(any())).thenReturn(validarUserResponse);
        when(validarAssociadoVotouEmPautaService.validar(any(), any())).thenReturn(Boolean.FALSE);
        when(pautaRepository.findById(any())).thenReturn(Optional.of(pautaEntity));
        when(buscarSessaoVotacaoAbertaService.buscar(any())).thenReturn(Optional.of(sessaoVotacaoEntity));

        assertDoesNotThrow(() -> service.cadastrar(pautaId, request));

        verify(votoAssociadoRepository).save(captor.capture());
        verify(userFeign).validarUser(request.associadoCpf());
        verify(validarAssociadoVotouEmPautaService).validar(pautaId, request.associadoCpf());
        verify(pautaRepository).findById(pautaId);
        verify(buscarSessaoVotacaoAbertaService).buscar(pautaId);

        final var captorValue = captor.getValue();
        assertEquals(request.associadoCpf(), captorValue.getAssociadoCpf());
        assertEquals(request.voto(), captorValue.getVoto());
        assertEquals(sessaoVotacaoEntity, captorValue.getSessaoVotacao());
    }

    @Test
    void deveNaoCadastrarVotoComUsuarioSemPermissao() {
        final Long pautaId = random.nextLong();
        final var validarUserResponse = ValidarUserResponse.builder()
            .status(StatusVotacao.UNABLE_TO_VOTE)
            .build();

        when(userFeign.validarUser(any())).thenReturn(validarUserResponse);

        assertThrows(BusinessException.class, () -> service.cadastrar(pautaId, request));

        verify(userFeign).validarUser(request.associadoCpf());
        verifyNoInteractions(votoAssociadoRepository, validarAssociadoVotouEmPautaService,
            pautaRepository, buscarSessaoVotacaoAbertaService);
    }

    @Test
    void deveNaoCadastrarVotoComUsuarioCpfInvalido() {
        final Long pautaId = random.nextLong();

        when(userFeign.validarUser(any())).thenThrow(FeignException.NotFound.class);

        assertThrows(BusinessException.class, () -> service.cadastrar(pautaId, request));

        verify(userFeign).validarUser(request.associadoCpf());
        verifyNoInteractions(votoAssociadoRepository, validarAssociadoVotouEmPautaService,
            pautaRepository, buscarSessaoVotacaoAbertaService);
    }

    @Test
    void deveNaoCadastrarVotoComFalhaAoValidarUsuario() {
        final Long pautaId = random.nextLong();

        when(userFeign.validarUser(any())).thenThrow(FeignException.class);

        assertThrows(BusinessException.class, () -> service.cadastrar(pautaId, request));

        verify(userFeign).validarUser(request.associadoCpf());
        verifyNoInteractions(votoAssociadoRepository, validarAssociadoVotouEmPautaService,
            pautaRepository, buscarSessaoVotacaoAbertaService);
    }

    @Test
    void deveNaoCadastrarVotoQuandoUsuarioJaVotou() {
        final Long pautaId = random.nextLong();
        final var validarUserResponse = ValidarUserResponse.builder().build();

        when(userFeign.validarUser(any())).thenReturn(validarUserResponse);
        when(validarAssociadoVotouEmPautaService.validar(any(), any())).thenReturn(Boolean.TRUE);

        assertThrows(BusinessException.class, () -> service.cadastrar(pautaId, request));

        verify(userFeign).validarUser(request.associadoCpf());
        verify(validarAssociadoVotouEmPautaService).validar(pautaId, request.associadoCpf());
        verifyNoInteractions(votoAssociadoRepository, pautaRepository, buscarSessaoVotacaoAbertaService);
    }

    @Test
    void deveNaoCadastrarVotoQuandoPautaInexistente() {
        final Long pautaId = random.nextLong();
        final var validarUserResponse = ValidarUserResponse.builder().build();

        when(userFeign.validarUser(any())).thenReturn(validarUserResponse);
        when(validarAssociadoVotouEmPautaService.validar(any(), any())).thenReturn(Boolean.FALSE);
        when(pautaRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.cadastrar(pautaId, request));

        verify(userFeign).validarUser(request.associadoCpf());
        verify(validarAssociadoVotouEmPautaService).validar(pautaId, request.associadoCpf());
        verify(pautaRepository).findById(pautaId);
        verifyNoInteractions(votoAssociadoRepository, buscarSessaoVotacaoAbertaService);
    }

    @Test
    void deveNaoCadastrarVotoQuandoSessaoInexistente() {
        final Long pautaId = random.nextLong();
        final var pautaEntity = PautaEntityStub.generate().build();
        final var validarUserResponse = ValidarUserResponse.builder().build();

        when(userFeign.validarUser(any())).thenReturn(validarUserResponse);
        when(validarAssociadoVotouEmPautaService.validar(any(), any())).thenReturn(Boolean.FALSE);
        when(pautaRepository.findById(any())).thenReturn(Optional.of(pautaEntity));
        when(buscarSessaoVotacaoAbertaService.buscar(any())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.cadastrar(pautaId, request));

        verify(userFeign).validarUser(request.associadoCpf());
        verify(validarAssociadoVotouEmPautaService).validar(pautaId, request.associadoCpf());
        verify(pautaRepository).findById(pautaId);
        verify(buscarSessaoVotacaoAbertaService).buscar(pautaId);
        verifyNoInteractions(votoAssociadoRepository);
    }
}