package br.com.alugamais.dao;


import br.com.alugamais.web.domain.Pagamento;
import br.com.alugamais.web.domain.Parcela;
import br.com.alugamais.web.util.PaginacaoUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public interface ParcelaDao {

    void save(Parcela Parcela);

    void update(Parcela Parcela);

    void delete(Long id);

    Parcela findById(Long id);

    List<Parcela> findAll();



    PaginacaoUtil<Parcela> buscaPaginada(int pagina, String direcao, Long idContrato);

    List<Parcela> findParcelasVencidasEAVencer(Long contrato, int dias);

    List<Parcela> listaParcela(Long contrato, int dias);

    List<LocalDate> findDatasPeriodo(Long contrato, int dias);

    void saveAll(List<Parcela> diaries);

    void atualizaSituacaoParcela(Pagamento pagamento, int dias, String situacao);

    int getUltimaParcelaParcelaPaga(Long idContrato);

    void voltarParcelasPagas(Long pagamentoId);

    BigDecimal extratoParcelasContrato(Long contratoId, String situacao);

    LocalDate getUltimaDataParcela(Long IdContrato);

    int getUltimaNumeroParcela(Long IdContrato);

    BigDecimal sumTotalContrato(Long idContrato);

    BigDecimal faturamentoParcelaEmpresa(Long locadorId);

    void reduzirPeriodo(Long contrato, String limit);

    long countParcelaRestante(Long idContrato);

    long countParcelaIsentas(Long idContrato);
}
