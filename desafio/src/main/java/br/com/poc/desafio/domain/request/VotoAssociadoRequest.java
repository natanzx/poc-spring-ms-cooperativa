package br.com.poc.desafio.domain.request;

import br.com.poc.desafio.domain.type.VotoType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record VotoAssociadoRequest(
    @Schema(description = "CPF do associado", example = "20479572062")
    @NotNull String associadoCpf,
    @Schema(description = "Opção de voto", example = "SIM")
    @NotNull VotoType voto
) {

}
