package br.com.alugamais.dao;

import br.com.alugamais.web.domain.Locador;

import java.util.List;

public interface LocadorDao {

    void save(Locador Locador);

    void update(Locador Locador);

    void delete(Long id);

    Locador findById(Long id);

    List<Locador> findAll();
}
