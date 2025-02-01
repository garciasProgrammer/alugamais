package br.com.alugamais.dao;

import br.com.alugamais.web.domain.Locatario;
import org.springframework.stereotype.Repository;

@Repository
public class LocatarioDaoImpl  extends AbstractDao<Locatario, Long> implements LocatarioDao{

    public Object getClientes() {
        return getEntityManager().createQuery("SELECT count(i.id) FROM Locatario i")
                .getSingleResult();
    }
}
