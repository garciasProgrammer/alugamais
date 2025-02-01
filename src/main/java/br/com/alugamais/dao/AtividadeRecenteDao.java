package br.com.alugamais.dao;

import br.com.alugamais.web.domain.AtividadeRecente;
import br.com.alugamais.web.domain.Imovel;

import java.util.List;

public interface AtividadeRecenteDao {

    void save(AtividadeRecente atividadeRecente);

    void update(AtividadeRecente atividadeRecente);

    void delete(Long id);

    AtividadeRecente findById(Long id);

    List<AtividadeRecente> findAll();

    List<AtividadeRecente> getAtividades();

}
