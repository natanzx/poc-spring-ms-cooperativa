package br.com.poc.desafio.mobile.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.poc.desafio.mobile.client.ApiFeign;
import br.com.poc.desafio.mobile.client.response.PautaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListarPautasService {

    private final ApiFeign apiFeign;

    public List<PautaResponse> listar() {
        log.info("Listando pautas cadastradas na API");

        return apiFeign.listarPautas();
    }
}
