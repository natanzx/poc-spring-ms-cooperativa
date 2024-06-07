package br.com.poc.desafio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import br.com.poc.desafio.domain.request.PautaRequest;
import br.com.poc.desafio.domain.request.SessaoVotacaoRequest;
import br.com.poc.desafio.domain.request.VotoAssociadoRequest;
import br.com.poc.desafio.domain.response.PautaConsolidadaResponse;
import br.com.poc.desafio.domain.response.PautaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Pauta", description = "Serviços responsáveis por gerenciar a pauta de uma Assembleia")
public interface ApiPauta {

    @Operation(summary = "Cadastra uma pauta da assembleia.",
        description = "Cadastra pauta para que inicie uma sessão de votação.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "CPF inválido ou não informado.</li>"),
            @ApiResponse(responseCode = "401", description = "Não autorizado</li></ul>"),
            @ApiResponse(responseCode = "500", description = "Falha inesperada")
        }
    )
    PautaResponse cadastrarPauta(final PautaRequest request);

    @Operation(summary = "Lista pautas cadastradas.",
        description = "Lista todas as pautas cadastradas.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "CPF inválido ou não informado.</li>"),
            @ApiResponse(responseCode = "401", description = "Não autorizado</li></ul>"),
            @ApiResponse(responseCode = "500", description = "Falha inesperada")
        }
    )
    List<PautaResponse> listarPautas();

    @Operation(summary = "Cadastra uma sessão de votação para uma pauta da assembleia.",
        description = "A criação da sessão só será efetuada caso não haja outra sessão aberta em andamento."
            + "<br/> Será validada se a pauta existe.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "CPF inválido ou não informado.</li>"),
            @ApiResponse(responseCode = "401", description = "Não autorizado</li></ul>"),
            @ApiResponse(responseCode = "500", description = "Falha inesperada")
        }
    )
    void cadastrarSessaoVotacao(
        @Parameter(description = "Id da pauta", example = "1") final Long pautaId,
        final SessaoVotacaoRequest request);

    @Operation(summary = "Cadastra o voto de um associado em uma pauta.",
        description = "Cada associado só poderá votar uma vez em cada assembleia."
            + "<br/> Deve haver uma sessão aberta.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "CPF inválido ou não informado.</li>"),
            @ApiResponse(responseCode = "401", description = "Não autorizado</li></ul>"),
            @ApiResponse(responseCode = "500", description = "Falha inesperada")
        }
    )
    void cadastrarVotoAssociado(
        @Parameter(description = "Id da pauta", example = "1") final Long pautaId,
        final VotoAssociadoRequest request);

    @Operation(summary = "Consolida os votos de uma pauta.",
        description = "Efetua a contagem de votos e retorna o resultado.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "CPF inválido ou não informado.</li>"),
            @ApiResponse(responseCode = "401", description = "Não autorizado</li></ul>"),
            @ApiResponse(responseCode = "500", description = "Falha inesperada")
        }
    )
    ResponseEntity<PautaConsolidadaResponse> consolidarPauta(
        @Parameter(description = "Id da pauta", example = "1") final Long pautaId);
}
