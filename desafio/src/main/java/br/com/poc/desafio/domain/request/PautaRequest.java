package br.com.poc.desafio.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record PautaRequest(
    @Schema(description = "Titulo da pauta", example = "Assembleia dos amigos")
    @NotEmpty String titulo
) {

}
