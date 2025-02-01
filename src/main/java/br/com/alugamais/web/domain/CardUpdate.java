package br.com.alugamais.web.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardUpdate {
    private Object titulo;
    private Object mensagem;
    private Object action;

    private Object contratoId;


    public CardUpdate(Object titulo, Object mensagem, Object action, Object contratoId) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.action = action;
        this.contratoId = contratoId;
    }



}
