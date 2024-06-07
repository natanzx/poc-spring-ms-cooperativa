package br.com.poc.desafio.mobile.domain.response;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class TelaResponse {

    private TelaType tipo;
    private String titulo;
    private List<ItemTela> itens;
    private ItemTela botaoOk;
    private ItemTela botaoCancelar;

    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(Include.NON_NULL)
    public static class ItemTela {

        private InputType tipo;
        private String texto;
        private String id;
        private String titulo;
        private String valor;
        private String url;
        private HashMap<String, String> body;

    }

    public enum InputType {
        TEXTO,
        INPUT_TEXTO,
        INPUT_NUMERO,
        INPUT_DATA
    }

    public enum TelaType {
        FORMULARIO,
        SELECAO
    }
}
