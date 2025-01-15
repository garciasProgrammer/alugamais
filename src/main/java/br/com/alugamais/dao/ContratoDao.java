package br.com.alugamais.dao;

import br.com.alugamais.web.domain.Contrato;

import java.util.List;

public interface ContratoDao {


    void save(Contrato cliente);

    void update(Contrato cliente);

    void delete(Long id);

    Contrato findById(Long id);

    List<Contrato> findAll();

}
