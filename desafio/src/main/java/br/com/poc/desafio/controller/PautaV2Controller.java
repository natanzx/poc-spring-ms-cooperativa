package br.com.poc.desafio.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.poc.desafio.domain.response.PautaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Pauta", description = "Serviços responsáveis por gerenciar a pauta de uma Assembleia")
@RestController
@RequestMapping("/v2/pautas")
public class PautaV2Controller {

    @Operation(summary = "Lista pautas cadastradas.",
        description = "Lista todas as pautas cadastradas.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "CPF inválido ou não informado.</li>"),
            @ApiResponse(responseCode = "401", description = "Não autorizado</li></ul>"),
            @ApiResponse(responseCode = "500", description = "Falha inesperada")
        }
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<PautaResponse> listarPautas() {
        return Collections.singletonList(PautaResponse.builder().build());
    }

}
