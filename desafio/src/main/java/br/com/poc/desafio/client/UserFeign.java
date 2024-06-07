package br.com.poc.desafio.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.poc.desafio.client.response.ValidarUserResponse;

@FeignClient(value = "API", url = "${app.url.api-users}")
public interface UserFeign {

    @GetMapping("/users/{cpf}")
    ValidarUserResponse validarUser(@PathVariable(name = "cpf") final String cpf);

}
