package br.com.poc.desafio.client.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidarUserResponse {

    private StatusVotacao status;

    public enum StatusVotacao {
        ABLE_TO_VOTE,
        UNABLE_TO_VOTE
    }
}
