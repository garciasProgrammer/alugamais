package br.com.alugamais.web.enums;

public enum TipoDeSituacaoParcela {


    A_VENCER("a vencer"),
    VENCIDA("vencida"),
    PAGA("paga");

    private String descricao;

    TipoDeSituacaoParcela(String descricao) {
        this.descricao = descricao;
    }
}


