package br.com.poc.desafio.domain.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record PautaResponse(
    Long id,
    String titulo,
    LocalDateTime dataCriacao
) {

}
