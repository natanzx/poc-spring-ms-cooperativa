package br.com.poc.desafio.mobile.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.poc.desafio.mobile.domain.response.TelaResponse;
import br.com.poc.desafio.mobile.service.TelaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PautaMobileController {

    private final TelaService telaService;

    @PostMapping("/")
    public TelaResponse listarTelas() {
        return telaService.telaInicial();
    }

    @PostMapping("/pautas")
    public TelaResponse cadastrarPauta() {
        return telaService.cadastrarPauta();
    }

    @PostMapping("/pautas/listar")
    public TelaResponse listarPauta() {
        return telaService.listarPauta();
    }

    @PostMapping("/sessoes")
    public TelaResponse cadastrarSessao() {
        return telaService.cadastrarSessao();
    }

    @PostMapping("/votos")
    public TelaResponse cadastrarVoto(
        @RequestParam("pautaId") String pautaId,
        @RequestParam("associadoId") String associadoId) {
        return telaService.cadastrarVoto(pautaId, associadoId);
    }
}
