package br.com.alugamais.dao;

import br.com.alugamais.web.domain.AtividadeRecente;
import br.com.alugamais.web.domain.Imovel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AtividadeRecenteDaoImpl extends AbstractDao<AtividadeRecente, Long> implements AtividadeRecenteDao {

    public List<AtividadeRecente> getAtividades() {
        List<AtividadeRecente> getAtividades = getEntityManager()
                .createNativeQuery("SELECT * FROM atividade_recente a ORDER BY a.id DESC", AtividadeRecente.class)
                .getResultList();
        return getAtividades;
    }

}
