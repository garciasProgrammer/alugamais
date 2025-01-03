package br.com.alugamais.web.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardUpdate {
    private Object tipo;
    private Object valor;


    public CardUpdate(Object tipo, Object valor) {
        this.tipo = tipo;
        this.valor = valor;
    }



}
