package br.com.alugamais.service;

import br.com.alugamais.web.domain.Contrato;

import java.util.List;

public interface ContratoService {

    void salvar(Contrato cliente);

    void editar(Contrato cliente);

    void excluir(Long id);

    Contrato buscarPorId(Long id);

    List<Contrato> buscarTodos();


}
