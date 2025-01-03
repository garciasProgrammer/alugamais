package br.com.alugamais.service;

import br.com.alugamais.web.domain.Pagamento;
import br.com.alugamais.web.domain.Parcela;
import br.com.alugamais.web.util.PaginacaoUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ParcelaService {

    void salvar(Parcela parcela);

    void editar(Parcela parcela);

    void excluir(Long id);

    Parcela buscarPorId(Long id);

    List<Parcela> buscarTodos();


    PaginacaoUtil buscaPaginada(int pagina, String direcao, Long idContrato);

    List<Parcela> findParcelasVencidasEAVencer(long contrato, int dias);


    List<Parcela> listaParcelas(Long contrato, int dias);

    List<LocalDate> findDatasPeriodo(Long contrato, int dias);

    void saveAll(List<Parcela> diaries);

    void atualizaSituacaoParcela(Pagamento pagamento, int dias, String situacao);

    int getUltimaParcelaParcelaPaga(Long IdContrato);

    void voltarParcelasPagas(Long pagamentoId);

    BigDecimal extratoParcelaContrato(Long contratoId, String situacao);

    LocalDate getUltimaDataParcela(Long IdContrato);

    int getUltimaNumeroParcela(Long IdContrato);

    BigDecimal sumTotalContrato(Long idContrato);

    BigDecimal faturamentoParcelaEmpresa(Long locadorId);

    void reduzirPeriodo(Long contrato, String limit);

    long countParcelaRestante(Long idContrato);

    long countParcelaIsentas(Long idContrato);

}
