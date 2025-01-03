package br.com.alugamais.web.enums;

public enum TipoDePagamento {


    ENTRADA("Entrada"),
    SAIDA("Saída");

    private String descricao;

    TipoDePagamento(String descricao) {
        this.descricao = descricao;
    }
}


