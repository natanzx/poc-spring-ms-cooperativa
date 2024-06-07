package br.com.poc.desafio.mock.domain.response;

import br.com.poc.desafio.mock.domain.StatusVotacao;
import lombok.Builder;

@Builder
public record ValidarUserResponse(
    StatusVotacao status
) {

}
