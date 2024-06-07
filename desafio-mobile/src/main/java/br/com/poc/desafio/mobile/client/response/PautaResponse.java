package br.com.poc.desafio.mobile.client.response;

import java.time.LocalDateTime;

public record PautaResponse(
    Long id,
    String titulo,
    LocalDateTime dataCriacao
) {

}
