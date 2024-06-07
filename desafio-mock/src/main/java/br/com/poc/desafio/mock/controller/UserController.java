package br.com.poc.desafio.mock.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.poc.desafio.mock.domain.StatusVotacao;
import br.com.poc.desafio.mock.domain.response.ValidarUserResponse;
import br.com.poc.desafio.mock.util.RandomUtils;
import br.com.poc.desafio.mock.validador.CPFValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserController {

    @GetMapping("/users/{cpf}")
    public ResponseEntity<ValidarUserResponse> validar(@PathVariable(name = "cpf") final String cpf) {
        if (CPFValidator.isValid(cpf)) {
            final var status = RandomUtils.pickRandom(StatusVotacao.values());

            log.info("Cpf {} válido, permissao: {}", cpf, status);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(ValidarUserResponse.builder()
                    .status(status)
                    .build()
                );
        }

        log.warn("Cpf {} inválido", cpf);
        return ResponseEntity.notFound().build();
    }
}
