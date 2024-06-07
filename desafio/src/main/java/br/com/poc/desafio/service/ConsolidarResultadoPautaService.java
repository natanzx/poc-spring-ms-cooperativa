package br.com.poc.desafio.service;

import org.springframework.stereotype.Service;

import br.com.poc.desafio.domain.response.PautaConsolidadaResponse;
import br.com.poc.desafio.domain.type.VotoType;
import br.com.poc.desafio.exception.core.BusinessException;
import br.com.poc.desafio.repository.PautaRepository;
import br.com.poc.desafio.repository.VotoAssociadoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsolidarResultadoPautaService {

    private final PautaRepository pautaRepository;
    private final VotoAssociadoRepository votoRepository;

    public PautaConsolidadaResponse consolidar(final Long pautaId) {
        log.info("Consolidando resultado da pauta {}", pautaId);

        final var pautaEntity = pautaRepository.findById(pautaId)
            .orElseThrow(() -> new BusinessException("Pauta não existente"));

        final var quantidadeVotosSim = votoRepository.countBySessaoVotacao_PautaIdAndVoto(pautaId, VotoType.SIM);
        final var quantidadeVotosNao = votoRepository.countBySessaoVotacao_PautaIdAndVoto(pautaId, VotoType.NAO);

        return PautaConsolidadaResponse.builder()
            .titulo(pautaEntity.getTitulo())
            .dataCriacao(pautaEntity.getDataCriacao())
            .quantidadeVotosSim(quantidadeVotosSim)
            .quantidadeVotosNao(quantidadeVotosNao)
            .build();
    }
}
