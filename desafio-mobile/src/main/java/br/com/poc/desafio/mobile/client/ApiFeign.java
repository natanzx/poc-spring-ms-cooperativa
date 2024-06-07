package br.com.poc.desafio.mobile.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.poc.desafio.mobile.client.response.PautaResponse;

@FeignClient(value = "API", url = "${app.url.api}")
public interface ApiFeign {

    @GetMapping("/pautas")
    List<PautaResponse> listarPautas();
}
