package br.com.poc.desafio.service;

import org.springframework.stereotype.Service;

import br.com.poc.desafio.domain.entity.PautaEntity;
import br.com.poc.desafio.domain.request.PautaRequest;
import br.com.poc.desafio.domain.response.PautaResponse;
import br.com.poc.desafio.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CadastrarPautaService {

    private final PautaRepository repository;

    public PautaResponse cadastrar(final PautaRequest request) {
        log.info("Cadastrando nova pauta: {}", request.toString());

        final PautaEntity entity = PautaEntity.builder()
            .titulo(request.titulo())
            .build();

        final var savedEntity = repository.save(entity);

        return PautaResponse.builder()
            .id(savedEntity.getId())
            .titulo(savedEntity.getTitulo())
            .dataCriacao(savedEntity.getDataCriacao())
            .build();
    }
}
