package br.com.alugamais.service;

import br.com.alugamais.dao.ParcelaDao;
import br.com.alugamais.web.domain.Pagamento;
import br.com.alugamais.web.domain.Parcelas;
import br.com.alugamais.web.util.PaginacaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional()
public class ParcelaServiceImpl implements ParcelaService {

    @Autowired
    ParcelaDao dao;

    @Override
    public void salvar(Parcelas parcela) {
        dao.save(parcela);

    }

    @Override
    public void editar(Parcelas parcela) {
        dao.update(parcela);

    }

    @Override
    public void excluir(Long id) {
        dao.delete(id);

    }

    @Override
    @Transactional(readOnly = true)
    public Parcelas buscarPorId(Long id) {

        return dao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Parcelas> buscarTodos() {

        return dao.findAll();
    }


    public PaginacaoUtil buscaPaginada(int pagina, String direcao, Long idContrato) {
        return dao.buscaPaginada(pagina, direcao, idContrato);
    }

    public List<Parcelas> findParcelasVencidasEAVencer(long contrato, int dias) {
        return dao.findParcelasVencidasEAVencer(contrato, dias);
    }

    public List<Parcelas> listaParcelas(Long contrato, int dias) {
        return dao.listaParcela(contrato, dias);
    }

    @Override
    public List<LocalDate> findDatasPeriodo(Long contrato, int dias) {
        return dao.findDatasPeriodo(contrato, dias);
    }

    @Override
    public void saveAll(List<Parcelas> parcels) {
        dao.saveAll(parcels);
    }


    public void atualizaSituacaoParcela(Pagamento pagamento, int dias, String situacao) {
        dao.atualizaSituacaoParcela(pagamento, dias, situacao);
    }

    @Override
    public int getUltimaParcelaParcelaPaga(Long idContrato) {
        return dao.getUltimaParcelaParcelaPaga(idContrato);
    }


    @Override
    public void voltarParcelasPagas(Long pagamentoId) {
        dao.voltarParcelasPagas(pagamentoId);
    }

    @Override
    public BigDecimal extratoParcelaContrato(Long contratoId, String situacao) {
        return dao.extratoParcelasContrato(contratoId, situacao);
    }

    @Override
    public LocalDate getUltimaDataParcela(Long IdContrato) {
        return dao.getUltimaDataParcela(IdContrato);
    }

    @Override
    public int getUltimaNumeroParcela(Long IdContrato) {
        return dao.getUltimaNumeroParcela(IdContrato);
    }
    @Override
    public BigDecimal sumTotalContrato(Long idContrato) {
        return dao.sumTotalContrato(idContrato);
    }

    @Override
    public BigDecimal faturamentoParcelaEmpresa(Long locadorId){
        return dao.faturamentoParcelaEmpresa(locadorId);
    }

    @Override
    public void reduzirPeriodo(Long contrato, String limit) {
        dao.reduzirPeriodo(contrato, limit);
    }

    @Override
    public long countParcelaRestante(Long idContrato){
        return dao.countParcelaRestante(idContrato);
    }

    @Override
    public long countParcelaIsentas(Long idContrato){
        return dao.countParcelaIsentas(idContrato);
    }

    @Override
    public List<Parcelas> buscaParcelas(Long idContrato){
        return dao.buscaParcelas(idContrato);
    }

    @Override
    public List<Parcelas> getParcela(Long contratoId, String numParcela){
        return dao.getParcela(contratoId, numParcela);
    }
}
