package br.com.poc.desafio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.poc.desafio.domain.response.PautaResponse;
import br.com.poc.desafio.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListarPautasService {

    private final PautaRepository pautaRepository;

    public List<PautaResponse> listar() {
        log.info("Listando pautas cadastradas");

        return pautaRepository.findAll().stream()
            .map(pauta -> PautaResponse.builder()
                .id(pauta.getId())
                .titulo(pauta.getTitulo())
                .dataCriacao(pauta.getDataCriacao())
                .build())
            .collect(Collectors.toList());
    }
}
