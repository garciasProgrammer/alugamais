package br.com.alugamais.dao;


import br.com.alugamais.web.domain.Pagamento;
import br.com.alugamais.web.domain.Parcelas;
import br.com.alugamais.web.util.PaginacaoUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public interface ParcelaDao {

    void save(Parcelas Parcela);

    void update(Parcelas Parcela);

    void delete(Long id);

    Parcelas findById(Long id);

    List<Parcelas> findAll();



    PaginacaoUtil<Parcelas> buscaPaginada(int pagina, String direcao, Long idContrato);

    List<Parcelas> findParcelasVencidasEAVencer(Long contrato, int dias);

    List<Parcelas> listaParcela(Long contrato, int dias);

    List<LocalDate> findDatasPeriodo(Long contrato, int dias);

    void saveAll(List<Parcelas> diaries);

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
