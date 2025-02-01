package br.com.alugamais.dao;

import br.com.alugamais.web.domain.Locatario;
import java.util.List;

public interface LocatarioDao {

    void save(Locatario Locatario);

    void update(Locatario Locatario);

    void delete(Long id);

    Locatario findById(Long id);

    List<Locatario> findAll();

    Object getClientes();
}
