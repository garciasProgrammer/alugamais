package br.com.alugamais.service;

import br.com.alugamais.web.domain.Pagamento;
import br.com.alugamais.web.domain.Parcelas;
import br.com.alugamais.web.util.PaginacaoUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ParcelaService {

    void salvar(Parcelas parcela);

    void editar(Parcelas parcela);

    void excluir(Long id);

    Parcelas buscarPorId(Long id);

    List<Parcelas> buscarTodos();


    PaginacaoUtil buscaPaginada(int pagina, String direcao, Long idContrato);

    List<Parcelas> findParcelasVencidasEAVencer(long contrato, int dias);


    List<Parcelas> listaParcelas(Long contrato, int dias);

    List<LocalDate> findDatasPeriodo(Long contrato, int dias);

    void saveAll(List<Parcelas> parcels);

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
