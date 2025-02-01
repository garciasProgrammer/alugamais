package br.com.alugamais.service;

import br.com.alugamais.web.domain.Parcelas;
import br.com.alugamais.web.domain.Pix;

import java.util.List;


public interface PixService {


    void salvar(Pix pix);

    void editar(Pix pix);

    void excluir(Long id);

    Pix buscarPorId(Long id);

    List<Pix> buscarTodos();

    List<Pix> getDadosPix(String pixQrCodeId);
}
