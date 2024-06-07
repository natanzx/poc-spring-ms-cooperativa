package br.com.poc.desafio.service;

import static java.util.Optional.ofNullable;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.com.poc.desafio.domain.entity.SessaoVotacaoEntity;
import br.com.poc.desafio.domain.request.SessaoVotacaoRequest;
import br.com.poc.desafio.exception.core.BusinessException;
import br.com.poc.desafio.repository.PautaRepository;
import br.com.poc.desafio.repository.SessaoVotacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CadastrarSessaoVotacaoService {

    private static final Integer UM_MINUTO = 1;

    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final PautaRepository pautaRepository;
    private final BuscarSessaoVotacaoAbertaService buscarSessaoVotacaoAbertaService;

    public void cadastrar(final Long pautaId, final SessaoVotacaoRequest request) {
        final var pautaEntity = pautaRepository.findById(pautaId)
            .orElseThrow(() -> new BusinessException("Pauta não existente"));

        buscarSessaoVotacaoAbertaService.buscar(pautaId)
            .ifPresent(sessao -> {
                throw new BusinessException("Já existe uma sessão de votação em aberto");
            });

        log.info("Cadastrando nova sessão de votação na pauta {}", pautaId);

        final LocalDateTime dataFim = LocalDateTime.now()
            .plusMinutes(ofNullable(request)
                .map(SessaoVotacaoRequest::tamanhoSessaoEmMinutos)
                .orElse(UM_MINUTO));

        final SessaoVotacaoEntity entity = SessaoVotacaoEntity.builder()
            .pauta(pautaEntity)
            .dataInicio(LocalDateTime.now())
            .dataFim(dataFim)
            .build();

        sessaoVotacaoRepository.save(entity);
    }
}
