package br.com.alugamais.dao;

import br.com.alugamais.web.domain.Pagamento;
import br.com.alugamais.web.domain.Parcelas;
import br.com.alugamais.web.util.PaginacaoUtil;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Repository
public class ParcelaDaoImpl extends AbstractDao<Parcelas, Long> implements ParcelaDao {


    public List<Parcelas> listaParcela(Long contrato, int dias) {
        return getEntityManager()
                .createNativeQuery("select * from Parcelas d  where d.contrato_id_fk=:contrato limit :dias", Parcelas.class)
                .setParameter("contrato", contrato)
                .setParameter("dias", dias)
                .getResultList();
    }

    @Override
    public void saveAll(List<Parcelas> parcels) {
        for (Parcelas parcela : parcels) {
            getEntityManager().persist(parcela);
        }
    }

    public PaginacaoUtil<Parcelas> buscaPaginada(int pagina, String direcao, Long idContrato) {
        int tamanho = 14;
        int inicio = (pagina - 1) * tamanho;
        List<Parcelas> Parcelas = getEntityManager()
                .createQuery("select d from Parcela d  WHERE d.contrato.id =" + idContrato + " order by d.id " + direcao, Parcelas.class)
                .setFirstResult(inicio)
                .setMaxResults(tamanho)
                .getResultList();

        long totalRegistros = count(idContrato);
        long totalDePaginas = (totalRegistros + (tamanho - 1)) / tamanho;

        return new PaginacaoUtil<>(tamanho, pagina, totalDePaginas, direcao, Parcelas);
    }

    public List<Parcelas> buscaParcelas(Long idContrato) {

        List<Parcelas> Parcelas = getEntityManager()
                .createQuery("select d from Parcelas d  WHERE d.contrato.id =:idContrato  order by d.parcela asc", Parcelas.class)
                .setParameter("idContrato", idContrato)
                .getResultList();
        return Parcelas;
    }

    public int getUltimaParcelaParcelaPaga(Long IdContrato) {

        try {
            Object lastDiarie = getEntityManager()
                    .createNativeQuery("select d.parcela  from Parcelas d where d.situacao in ('PAGA','ISENTA') and d.contrato_id_fk = :contrato order by d.id desc limit 1")
                    .setParameter("contrato", IdContrato)
                    .getSingleResult();

            return Integer.parseInt(lastDiarie.toString());

        } catch (NoResultException noResultException) {
            return 0;
        }
    }

    public LocalDate getUltimaDataParcela(Long IdContrato) {
        java.sql.Date date = null;
        try {
            Object lastDiarie = getEntityManager()
                    .createNativeQuery("select d.data_vencimento from Parcelas d where d.contrato_id_fk = :contrato order by d.id desc limit 1")
                    .setParameter("contrato", IdContrato)
                    .getSingleResult();

            if (lastDiarie instanceof java.sql.Date) {
                date = (java.sql.Date) lastDiarie;

            }
            return date.toLocalDate();

        } catch (NoResultException noResultException) {
            return null;
        }
    }

    public int getUltimaNumeroParcela(Long IdContrato) {

        try {
            Object lastParcel = getEntityManager()
                    .createNativeQuery("select CAST(d.parcela AS unsigned)  from Parcelas d where d.contrato_id_fk = :contrato order by d.id desc limit 1")
                    .setParameter("contrato", IdContrato)
                    .getSingleResult();


            return Integer.parseInt(lastParcel.toString());

        } catch (NoResultException noResultException) {
            return 0;
        }
    }

    public long count(Long idContrato) {
        return getEntityManager()
                .createQuery("select count(d.parcela) from Parcelas d where d.contrato.id=" + idContrato, Long.class)
                .getSingleResult();
    }

    public BigDecimal sumTotalContrato(Long idContrato) {
        return getEntityManager()
                .createQuery("select SUM(d.valorAluguel) from Parcelas d where d.contrato.id=" + idContrato, BigDecimal.class)
                .getSingleResult();
    }

    public List<Parcelas> findParcelasVencidasEAVencer(Long contrato, int dias) {
        return getEntityManager()
                .createQuery("select d from Parcelas d where d.contrato.id=:contrato", Parcelas.class)
                .setParameter("contrato", contrato)
                .getResultList();
    }

    public List<Parcelas> findParcelasPagasNoPagamento(Long pagamentoId) {
        return getEntityManager()
                .createQuery("select d from Parcelas d where d.codigoPagamento=:pagamentoId", Parcelas.class)
                .setParameter("pagamentoId", String.valueOf(pagamentoId))
                .getResultList();
    }

    public List<LocalDate> findDatasPeriodo(Long contrato, int dias) {
        return getEntityManager()
                .createQuery("select d.dataVencimento from Parcelas d where d.contrato.id=:contrato and d.situacao not in ('paga','isenta') order by d.dataVencimento asc", LocalDate.class)
                .setParameter("contrato", contrato)
                .setFirstResult(0)
                .setMaxResults(dias)
                .getResultList();
    }

    public void atualizaSituacaoParcela(Pagamento pagamento, int dias, String situacao) {
        getEntityManager()
                .createNativeQuery("UPDATE Parcelas d SET d.situacao=:situacao, d.data_pagamento= :dataPagamento, d.codigoPagamento = :codigoPagamento " +
                        "WHERE d.situacao NOT IN ('PAGA','ISENTA') AND  d.contrato_id_fk = :contrato limit :dias")
                .setParameter("contrato", pagamento.getContrato().getId())
                .setParameter("codigoPagamento", pagamento.getId())
                .setParameter("dataPagamento", pagamento.getDataPagamento())
                .setParameter("situacao", situacao)
                .setParameter("dias", dias)
                .executeUpdate();
    }

    public void voltarParcelasPagas(Long pagamentoId) {
        getEntityManager().createQuery("UPDATE Parcelas dp SET dp.situacao='A VENCER', dp.dataPagamento=null, dp.codigoPagamento=null WHERE dp.codigoPagamento=:pagamentoId")
                .setParameter("pagamentoId", String.valueOf(pagamentoId))
                .executeUpdate();
    }

    public BigDecimal extratoParcelasContrato(Long contratoId, String situacao) {

        BigDecimal valor = getEntityManager().createQuery("SELECT SUM(d.valorAluguel) FROM Parcelas d WHERE d.situacao=:situacao AND d.contrato.id=:contratoId", BigDecimal.class)
                .setParameter("contratoId", contratoId)
                .setParameter("situacao", situacao)
                .getSingleResult();

        return valor != null ? valor : BigDecimal.ZERO;
    }

    public BigDecimal faturamentoParcelaEmpresa(Long locadorId) {
        try {
            List<BigDecimal> valor = getEntityManager().createQuery("select sum(d.valorAluguel) as total_Parcelas from Parcelas d left join Contrato c on d.contrato.id = c.id " +
                            "left join Locador l on c.locador.id = l.id " +
                            "where d.situacao in ('A VENCER', 'VENCIDA') " +
                            "and c.situacao = 'ABERTO' " +
                            "and l.id=:locadorId", BigDecimal.class)
                    .setParameter("locadorId", locadorId)
                    .getResultList();
            return valor.get(0);
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

    public void reduzirPeriodo(Long contrato, String limit) {
        // Converte o limit para inteiro e inverte o sinal para positivo
        int qtdId = Math.abs(Integer.parseInt(limit));

        // Consulta para selecionar os IDs a serem deletados
        List<Long> idsParaDeletar = getEntityManager().createNativeQuery("SELECT id FROM Parcelas " +
                        "WHERE contrato_id_fk = :contrato " +
                        "ORDER BY id DESC " +
                        "LIMIT :limit")
                .setParameter("contrato", contrato)
                .setParameter("limit", qtdId)
                .getResultList();

        // Verifica se há IDs para deletar
        if (!idsParaDeletar.isEmpty()) {
            // Operação de deleção usando os IDs selecionados
            getEntityManager().createNativeQuery("DELETE FROM Parcelas " +
                            "WHERE contrato_id_fk = :contrato AND id IN :ids")
                    .setParameter("contrato", contrato)
                    .setParameter("ids", idsParaDeletar)
                    .executeUpdate();
        }
    }

    public long countParcelaRestante(Long idContrato) {
        return getEntityManager()
                .createQuery("select count(d.parcela) " +
                        "from Parcelas d " +
                        "where d.contrato.id=:idContrato and d.situacao in ('A VENCER','VENCIDA')", Long.class)
                .setParameter("idContrato", idContrato)
                .getSingleResult();
    }

    public long countParcelaIsentas(Long idContrato) {
        return getEntityManager()
                .createQuery("select count(d.parcela) " +
                        "from Parcelas d " +
                        "where d.contrato.id=:idContrato and d.situacao ='ISENTA'", Long.class)
                .setParameter("idContrato", idContrato)
                .getSingleResult();
    }

    public List<Parcelas> getParcela(Long contratoId, String numParcela) {
        List<Parcelas> parcela = getEntityManager().createQuery("SELECT p " +
                        "FROM Parcelas p " +
                        "WHERE p.contrato.id=:contratoId " +
                        "AND p.parcela=:numParcela", Parcelas.class)
                .setParameter("contratoId", contratoId)
                .setParameter("numParcela", numParcela)
                .getResultList();

        return parcela;
    }

}
