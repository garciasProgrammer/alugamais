package br.com.alugamais.dao;

import br.com.alugamais.web.domain.Imovel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImovelDaoImpl extends AbstractDao<Imovel, Long> implements ImovelDao {

    public List<Imovel> getImoveisPorLocador(List<String> situacao, Long locadorId) {
        return getEntityManager().createQuery("SELECT i FROM Imovel i WHERE i.locador.id =:locadorId AND i.situacao in :situacao")
                .setParameter("locadorId", locadorId)
                .setParameter("situacao", situacao)
                .getResultList();
    }

    public Object getImoveisALugados() {
        return getEntityManager().createQuery("SELECT count(i.id) FROM Imovel i WHERE  i.situacao='Alugado'")
                .getSingleResult();
    }

    public Double getImoveisAlugadosPorcentagem() {
        Object result = getEntityManager().createNativeQuery(
                        "SELECT (COUNT(CASE WHEN situacao = 'Alugado' THEN 1 END) * 100.0 / COUNT(*)) " +
                                "FROM imovel")
                .getSingleResult();

        return result != null ? ((Number) result).doubleValue() : 0.0;
    }



}
