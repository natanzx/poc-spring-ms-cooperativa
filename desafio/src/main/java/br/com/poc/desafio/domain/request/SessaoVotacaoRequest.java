package br.com.poc.desafio.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

public record SessaoVotacaoRequest(
    @Schema(description = "Tempo da sessão em minutos", example = "2")
    @Positive Integer tamanhoSessaoEmMinutos
) {

}
