package br.com.alugamais.web.enums;

public enum TipoDeUsuario {

    GERENTE("Gerente"),
    ADMINISTRADOR("Administrador"),
    BASICO("Básico");

    private String descricao;

    TipoDeUsuario(String descricao) {
        this.descricao = descricao;
    }
}
