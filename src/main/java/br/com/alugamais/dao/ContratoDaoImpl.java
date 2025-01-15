package br.com.alugamais.dao;

import br.com.alugamais.web.domain.Contrato;
import br.com.alugamais.web.util.DataUtil;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ContratoDaoImpl extends AbstractDao<Contrato, Long> implements ContratoDao {

    DataUtil dataUtil = new DataUtil();

    public List<Contrato> findByNome(String nome) {

        return createQuery("select c from Contrato c where  c.cliente.nome like concat('%',?1,'%') ", nome);
    }

    public List<Contrato> findByListRenovacao() {

        return createQuery("select c from Contrato c where  c.situacao = 'ABERTO' order by c.dataRenovacao asc");
    }

    public String findByDataFinal(String dataIncial, String periodo, String tipoModalidade) {

        String dataFormatada[] = dataIncial.split("-");
        String _dataFormatadada = dataFormatada[2] + "/" + dataFormatada[1] + "/" + dataFormatada[0];

        if (tipoModalidade.equals("AVULSO")) {
            return dataUtil.formataData(dataUtil.calcularDataDeVencimentoAluguel(dataUtil.formataDataStringToCalendar(_dataFormatadada),
                    Integer.parseInt(periodo)).getTime());
        } else {
            return dataUtil.formataData(dataUtil.calcularDataDeVencimento(dataUtil.formataDataStringToCalendar(_dataFormatadada),
                    Integer.parseInt(periodo)).getTime());
        }
    }

    public String findByDataRenovacao(String dataIncial, String periodo, String tipoModalidade) {

        String dataFormatada[] = dataIncial.split("-");
        String _dataFormatadada = dataFormatada[2] + "/" + dataFormatada[1] + "/" + dataFormatada[0];
        if (tipoModalidade.equals("AVULSO")) {
            return dataUtil.formataData(dataUtil.calcularDataDeVencimentoAluguel(dataUtil.formataDataStringToCalendar(_dataFormatadada),
                    Integer.parseInt(periodo)).getTime());
        } else {
            return dataUtil.formataData(dataUtil.calcularDataDeVencimento(dataUtil.formataDataStringToCalendar(_dataFormatadada),
                    Integer.parseInt(periodo)).getTime());
        }

    }

    public List<Contrato> buscarPorLocador(Long locador) {

        return createQuery("select c from Contrato c where  c.locador.id = ?1", locador);

    }

    public long qtdContratos() {
        return createQuerySingleResultInt("SELECT COUNT(*) FROM  Contrato c WHERE c.situacao = 'ABERTO'");
    }


    public List<String> qtdContratosPorGrupo() {
        return getEntityManager().createNativeQuery("WITH situacoes AS (" +
                "    SELECT 'aberto' AS situacao " +
                "    UNION ALL" +
                "    SELECT 'cancelado'" +
                "    UNION ALL" +
                "    SELECT 'reservado'" +
                "    UNION ALL" +
                "    SELECT 'quitado'" +
                "    UNION ALL" +
                "    SELECT 'suspenso'" +
                ") " +
                "SELECT s.situacao, " +
                "       COALESCE(COUNT(c.id), 0) AS qtd " +
                "FROM situacoes s " +
                "LEFT JOIN contratos c ON c.situacao = s.situacao " +
                "GROUP BY s.situacao").getResultList();
    }


    public boolean clienteTemContrato(Long id) {
        return createBooleanQuery("SELECT COUNT(c) > 0 FROM Contrato c WHERE c.situacao = 'ABERTO' AND c.cliente.id = ?1 ", id);
    }

    public List<Contrato> findByPlacaVeiculo(Long id) {
        return createQuery("SELECT c FROM Contrato c WHERE c.veiculo.id= ?1", id);
    }

    public List<Contrato> recuperaContratoPorPlaca(String placa, String situacao) {
        return createQuery("SELECT c FROM Contrato c LEFT JOIN Veiculo v ON c.veiculo.id = v.id WHERE v.placa=?1 and c.situacao=?2", placa, situacao);
    }

    public List<Contrato> recuperaContratoPorClientId(Long clienteId) {
        return createQuery("SELECT c FROM Contrato c WHERE c.cliente.id=?1 and c.situacao='ABERTO'", clienteId);
    }

}
