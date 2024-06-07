package br.com.poc.desafio.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.poc.desafio.domain.entity.SessaoVotacaoEntity;
import br.com.poc.desafio.repository.SessaoVotacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
public class BuscarSessaoVotacaoAbertaService {

    private final SessaoVotacaoRepository repository;

    public Optional<SessaoVotacaoEntity> buscar(final Long pautaId) {
        final var dataAtual = LocalDateTime.now();

        return repository.findByPautaIdAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(
            pautaId, dataAtual, dataAtual);
    }
}
