package br.com.poc.desafio.mobile.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.poc.desafio.mobile.domain.response.TelaResponse;
import br.com.poc.desafio.mobile.domain.response.TelaResponse.InputType;
import br.com.poc.desafio.mobile.domain.response.TelaResponse.ItemTela;
import br.com.poc.desafio.mobile.domain.response.TelaResponse.TelaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelaService {

    @Value("${app.url.api}")
    private String urlApi;

    @Value("${app.url.mobile}")
    private String urlMobile;

    private final ListarPautasService listarPautasService;

    public TelaResponse telaInicial() {

        // TODO: ao cadastrar sessoes, listar todas as pautas e ver como selecionar o ID da pauta que quero cadastrar a sessao
        // TODO: ao cadastrar voto, selecionar qual pauta quero efetuar o voto, verificar como pegar o CPF do cliente

        final var cadastrarPauta = ItemTela.builder()
            .texto("Cadastrar pauta")
            .url(String.format("%s/pautas", urlMobile))
            .build();

        final var listarPautas = ItemTela.builder()
            .texto("Listar pautas")
            .url(String.format("%s/pautas/listar", urlMobile))
            .build();

        final var cadastrarSessao = ItemTela.builder()
            .texto("Cadastrar sessão")
            .url(String.format("%s/sessoes", urlMobile))
            .build();

        final var cadastrarVoto = ItemTela.builder()
            .texto("Cadastrar voto")
            .url(String.format("%s/votos", urlMobile))
            .build();

        return TelaResponse.builder()
            .tipo(TelaType.SELECAO)
            .titulo("Menu")
            .itens(Arrays.asList(cadastrarPauta, listarPautas, cadastrarSessao, cadastrarVoto))
            .build();
    }

    public TelaResponse cadastrarPauta() {

        final var descricaoPagina = criarDescricaoPagina("Cadastrar uma nova pauta para assembleia");

        final var inputTituloPauta = ItemTela.builder()
            .tipo(InputType.INPUT_TEXTO)
            .id("titulo")
            .titulo("Titulo da pauta")
            .valor(null)
            .build();

        final var botaoOk = ItemTela.builder()
            .texto("Confirmar")
            .url(String.format("%s/pautas", urlApi))
            .build();

        final var botaoCancelar = ItemTela.builder()
            .texto("Cancelar")
            .url(urlMobile)
            .build();

        return TelaResponse.builder()
            .tipo(TelaType.FORMULARIO)
            .titulo("CADASTRAR PAUTA")
            .itens(Arrays.asList(descricaoPagina, inputTituloPauta))
            .botaoOk(botaoOk)
            .botaoCancelar(botaoCancelar)
            .build();
    }

    public TelaResponse listarPauta() {

        final var pautas = listarPautasService.listar();

        final var itens = pautas.stream()
            .map(pauta -> ItemTela.builder()
                .texto(String.format("Pauta %d", pauta.id()))
                .url(urlMobile)
                .build())
            .collect(Collectors.toList());

        return TelaResponse.builder()
            .tipo(TelaType.SELECAO)
            .titulo("Pautas disponíveis")
            .itens(itens)
            .build();
    }

    public TelaResponse cadastrarSessao() {

        final var descricaoPagina = criarDescricaoPagina("Cadastrar uma nova sessão para uma pauta");

        final var inputPautaId = ItemTela.builder()
            .tipo(InputType.INPUT_TEXTO)
            .id("pautaId")
            .titulo("Pauta Id")
            .valor(null)
            .build();

        final var inputTempoSessaoEmMinutos = ItemTela.builder()
            .tipo(InputType.INPUT_NUMERO)
            .id("tamanhoSessaoEmMinutos")
            .titulo("Tempo da sessão em minutos")
            .valor(null)
            .build();

        final var botaoOk = ItemTela.builder()
            .texto("Confirmar")
            .url(String.format("%s/pautas/sessoes", urlApi))
            .build();

        final var botaoCancelar = ItemTela.builder()
            .texto("Cancelar")
            .url(urlMobile)
            .build();

        return TelaResponse.builder()
            .tipo(TelaType.FORMULARIO)
            .titulo("CADASTRAR SESSÃO")
            .itens(Arrays.asList(descricaoPagina, inputPautaId, inputTempoSessaoEmMinutos))
            .botaoOk(botaoOk)
            .botaoCancelar(botaoCancelar)
            .build();
    }

    public TelaResponse cadastrarVoto(final String pautaId, String associadoId) {

        final var opcaoSim = criarOpcao("Sim", "SIM", associadoId, pautaId);
        final var opcaoNao = criarOpcao("Não", "NAO", associadoId, pautaId);

        return TelaResponse.builder()
            .tipo(TelaType.SELECAO)
            .titulo("CADASTRAR VOTO")
            .itens(Arrays.asList(opcaoSim, opcaoNao))
            .build();
    }

    private ItemTela criarOpcao(String nome, String voto, String associadoCpf, String pautaId) {

        final var bodySim = new HashMap<String, String>();
        bodySim.put("voto", voto);
        bodySim.put("associadoCpf", associadoCpf);

        return ItemTela.builder()
            .texto(nome)
            .url(String.format("%s/pautas/%s/votos", urlApi, pautaId))
            .body(bodySim)
            .build();
    }

    private ItemTela criarDescricaoPagina(String descricao) {

        return ItemTela.builder()
            .tipo(InputType.TEXTO)
            .texto(descricao)
            .build();
    }
}
