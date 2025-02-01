package br.com.alugamais.web.controller;

import br.com.alugamais.service.AtividadeRecenteService;
import br.com.alugamais.service.ParcelaService;
import br.com.alugamais.service.PixService;
import br.com.alugamais.web.domain.AtividadeRecente;
import br.com.alugamais.web.domain.Pagamento;
import br.com.alugamais.web.domain.Parcelas;
import br.com.alugamais.web.domain.Pix;
import br.com.alugamais.web.util.PixUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pix")
public class PixController {

    @Autowired
    PixService pixService;
    @Autowired
    ParcelaService parcelaService;

    PixUtil pixUtil = new PixUtil();

    @Autowired
    private AtividadeRecenteService atividadeRecenteService;

    @PostMapping("/gerar-chave")
    public ResponseEntity<Map<String, Object>> gerarChavePagamentoPix(@RequestBody Pagamento pagamento) throws IOException {

        LocalDateTime dateExpiration = dataExpiracaoChavePix();
        List<Parcelas> parcelasList = parcelaService.getParcela(pagamento.getContrato().getId(), pagamento.getParcela().toString());
        Response responsePix = pixUtil.getPixKey(pagamento.getValorTotalLiquido(), dateExpiration);
        Map<String, Object> response = new HashMap<>();

        if (responsePix.isSuccessful()) {
            // Extrai os dados do JSON retornado pela API PIX
            String responseBody = responsePix.body().string(); // Lê o corpo da resposta
            ObjectMapper mapper = new ObjectMapper(); // Usa Jackson para manipular JSON
            JsonNode jsonNode = mapper.readTree(responseBody);

            // Assume que o JSON contém os campos 'qrCode' e 'copiaCola'
            String transactionId = jsonNode.get("id").asText();
            String qrCode = jsonNode.get("encodedImage").asText();
            String copiaCola = jsonNode.get("payload").asText();

            //salva valores de chavePix, copia e cola, contrato, parcela, horaExpiração, valorPix, situacao=AGUARDANDO_PAGAMENTO
            Pix pix = new Pix();
            pix.setTransactionId(transactionId);
            pix.setParcel(pagamento.getParcela().toString());
            pix.setSituation("AGUARDANDO_PAGAMENTO");
            pix.setAmount(pagamento.getValorTotalLiquido());
            pix.setDateCreate(LocalDate.now());
            pix.setDateExpiration(LocalDate.from(dateExpiration));
            pix.setCopyPasteKey(copiaCola);
            pix.setQrCodeKey(qrCode);
            pix.setLessor(pagamento.getContrato().getLocador().getId().toString());
            pix.setLessee(pagamento.getContrato().getLocatario().getId().toString());
            pix.setContract(pagamento.getContrato().getId().toString());
            pix.setDiscount(pagamento.getDesconto());
            pix.setUser("Thiago Batista Garcia");
            pix.setFeeValue(new BigDecimal(0.00));
            pix.setInterestValue(pagamento.getJuros());
            pix.setPaymentMethod(pagamento.getMeioPagamento());
            pix.setTypeOfPayment("ENTRADA");
            pix.setTypeOfDocument("ALUGUEL");
            pixService.salvar(pix);

            //log da atividade
            AtividadeRecente atividadeRecente = new AtividadeRecente();
            atividadeRecente.setTipoAtividade("CRIACAO_DE_CHAVE_PIX");
            atividadeRecente.setDataCriacao(LocalDateTime.now());
            atividadeRecente.setAtividade("Chave N°:" + pix.getTransactionId() +
                    ", Contrato N°:" + pix.getContract() +
                    ", referente parcela N°: " + pix.getParcel() +
                    ", valor R$" + pix.getAmount() +
                    ", data criação: " + LocalDate.now());
            atividadeRecenteService.salvar(atividadeRecente);

            // Retorna as informações da chave pix
            response.put("qrCode", qrCode);
            response.put("copiaCola", copiaCola);
            response.put("success", true);
        } else {
            response.put("success", false);
        }
        return ResponseEntity.ok(response);

    }

    private LocalDateTime dataExpiracaoChavePix() {
        LocalDateTime now = LocalDateTime.now();
        return now.plusMinutes(5);
    }

}
