package br.com.alugamais.service;

import br.com.alugamais.web.domain.AtividadeRecente;
import br.com.alugamais.web.domain.Imovel;

import java.util.List;

public interface AtividadeRecenteService {

    void salvar(AtividadeRecente atividadeRecente);

    void editar(AtividadeRecente atividadeRecente);

    void excluir(Long id);

    AtividadeRecente buscarPorId(Long id);

    List<AtividadeRecente> buscarTodos();

    List<AtividadeRecente> getAtividades();

}


