package br.com.alugamais.dao;

import br.com.alugamais.web.domain.Email;

import java.util.List;

public interface EmailDao {

    void save(Email enviarEmail);

    void update(Email enviarEmail);

    void delete(Long id);

    Email findById(Long id);

    List<Email> findAll();
}
