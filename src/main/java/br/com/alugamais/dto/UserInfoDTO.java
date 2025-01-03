package br.com.alugamais.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDTO {

    private Long usuarioLogadoId;
    private String usuarioLogado;

    public UserInfoDTO(){

    }

    public UserInfoDTO userInfo(Long usuarioLogadoId, String usuarioLogado){
        this.usuarioLogado = usuarioLogado;
        this.usuarioLogadoId = usuarioLogadoId;

        return this;
    }
}
