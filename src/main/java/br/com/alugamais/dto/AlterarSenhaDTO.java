package br.com.alugamais.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlterarSenhaDTO {

    private Long id;
    private String nome;
    private String userName;
    private String senhaAtual;
    private String novaSenha;
    private String confirmaSenha;
}
