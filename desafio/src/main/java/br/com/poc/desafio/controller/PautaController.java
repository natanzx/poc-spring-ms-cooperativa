package br.com.poc.desafio.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.poc.desafio.domain.request.PautaRequest;
import br.com.poc.desafio.domain.request.SessaoVotacaoRequest;
import br.com.poc.desafio.domain.request.VotoAssociadoRequest;
import br.com.poc.desafio.domain.response.PautaConsolidadaResponse;
import br.com.poc.desafio.domain.response.PautaResponse;
import br.com.poc.desafio.service.CadastrarPautaService;
import br.com.poc.desafio.service.CadastrarSessaoVotacaoService;
import br.com.poc.desafio.service.CadastrarVotoAssociadoService;
import br.com.poc.desafio.service.ConsolidarResultadoPautaService;
import br.com.poc.desafio.service.ListarPautasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/pautas")
@RequiredArgsConstructor
public class PautaController implements ApiPauta {

    private final CadastrarPautaService cadastrarPautaService;
    private final CadastrarSessaoVotacaoService cadastrarSessaoVotacaoService;
    private final CadastrarVotoAssociadoService cadastrarVotoAssociadoService;
    private final ConsolidarResultadoPautaService consolidarResultadoPautaService;
    private final ListarPautasService listarPautasService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PautaResponse cadastrarPauta(@RequestBody @Valid final PautaRequest request) {
        return cadastrarPautaService.cadastrar(request);
    }

    @GetMapping
    public List<PautaResponse> listarPautas() {
        return listarPautasService.listar();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{pautaId}/sessoes")
    public void cadastrarSessaoVotacao(
        @PathVariable(name = "pautaId") final Long pautaId,
        @RequestBody(required = false) @Valid final SessaoVotacaoRequest request) {
        cadastrarSessaoVotacaoService.cadastrar(pautaId, request);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{pautaId}/votos")
    public void cadastrarVotoAssociado(
        @PathVariable(name = "pautaId") final Long pautaId,
        @RequestBody @Valid final VotoAssociadoRequest request) {
        cadastrarVotoAssociadoService.cadastrar(pautaId, request);
    }

    @GetMapping("/{pautaId}/resultados")
    public ResponseEntity<PautaConsolidadaResponse> consolidarPauta(
        @PathVariable(name = "pautaId") final Long pautaId) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(consolidarResultadoPautaService.consolidar(pautaId));
    }
}
