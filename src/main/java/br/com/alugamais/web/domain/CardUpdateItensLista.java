package br.com.alugamais.web.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardUpdateItensLista {
    private Object tipo;
    private Object valorItens;
    private Object novaListaItens;


    public CardUpdateItensLista(Object tipo, Object valorItens, Object novaListaItens) {
        this.tipo = tipo;
        this.valorItens = valorItens;
        this.novaListaItens = novaListaItens;
    }


}
