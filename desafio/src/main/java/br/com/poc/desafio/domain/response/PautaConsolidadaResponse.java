package br.com.poc.desafio.domain.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record PautaConsolidadaResponse(
    String titulo,
    Integer quantidadeVotosSim,
    Integer quantidadeVotosNao,
    LocalDateTime dataCriacao
) {

}
