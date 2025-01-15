package br.com.alugamais.dao;

import br.com.alugamais.web.domain.Imovel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImovelDaoImpl extends AbstractDao<Imovel, Long> implements ImovelDao {

    public List<Imovel> getImoveisPorLocador(String situacao, Long locadorId) {
        return getEntityManager().createQuery("SELECT i FROM Imovel i WHERE i.locador.id =:locadorId AND i.situacao=:situacao")
                .setParameter("locadorId", locadorId)
                .setParameter("situacao", situacao)
                .getResultList();
    }
}
