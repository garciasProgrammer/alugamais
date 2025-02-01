package br.com.alugamais.service;

import br.com.alugamais.web.domain.Locatario;

import java.util.List;

public interface LocatarioService {

    void salvar(Locatario locatario);

    void editar(Locatario locatario);

    void excluir(Long id);

    Locatario buscarPorId(Long id);

    List<Locatario> buscarTodos();

    Object getClientes();
}
