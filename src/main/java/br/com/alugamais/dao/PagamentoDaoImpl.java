package br.com.alugamais.dao;

import br.com.alugamais.web.domain.Imovel;
import br.com.alugamais.web.domain.Pagamento;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class PagamentoDaoImpl extends AbstractDao<Pagamento, Long> implements PagamentoDao {

    public Object getPagamentosAnoMes() {
        Object valor = getEntityManager().createNativeQuery("select  SUM(p.valorTotalLiquido)" +
                        " from pagamento p " +
                        "where date_format(data_pagamento, '%Y%m')=date_format(now(), '%Y%m')")
                .getSingleResult();

        return valor != null ? ((Number) valor) : new BigDecimal(0);

    }

    public List<Pagamento> getPagamentosRecebidos() {
        List<Pagamento> pagamentos = getEntityManager().createNativeQuery("SELECT p.* " +
                        "FROM pagamento p " +
                        "where date_format(data_pagamento, '%Y%m')=date_format(now(), '%Y%m') ORDER BY p.id DESC", Pagamento.class)
                .getResultList();

        return pagamentos;

    }
}
