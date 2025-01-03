package br.com.alugamais.service;

import br.com.alugamais.web.domain.Locador;

import java.util.List;

public interface LocadorService {

    void salvar(Locador Locador);

    void editar(Locador Locador);

    void excluir(Long id);

    Locador buscarPorId(Long id);

    List<Locador> buscarTodos();
}
