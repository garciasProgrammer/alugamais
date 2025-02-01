package br.com.alugamais.web.controller;

import br.com.alugamais.service.*;
import br.com.alugamais.web.config.hibernate.TenantContext;
import br.com.alugamais.web.domain.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@PreAuthorize("PermitAll()")
@RequestMapping("/job")
public class JobController {

    @Autowired
    private PixService pixService;

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private PagamentoService pagamentoService;

    @Autowired
    private ParcelaService parcelaService;

    @Autowired
    private org.springframework.transaction.support.TransactionTemplate transactionTemplate;

    @Autowired
    private WebSocketController webSocketController;

    @Autowired
    private AtividadeRecenteService atividadeRecenteService;


    @PostMapping("/messages")
    public ResponseEntity<String> receiveWebhook(@RequestBody String payload) {
        try {
            CompletableFuture.runAsync(() -> {
                TenantContext.setCurrentTenant("residencial-sofia"); // Define o tenant antes da consulta

                transactionTemplate.execute(status -> {
                    processWebhook(payload);
                    return null;
                });

                TenantContext.clear(); // Limpa o tenant após o processamento
            });

            return ResponseEntity.ok("Recebido com sucesso");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao processar webhook");
        }
    }

    // Método separado para processar os dados em background
    private void processWebhook(String payload) {
        try {
            List<Pix> pix;
            JSONObject jsonObject = new JSONObject(payload);

            // Obtenha os valores do JSON
            JSONObject paymentObject = jsonObject.getJSONObject("payment");
            String pixQrCodeId = paymentObject.getString("pixQrCodeId");
            String status = paymentObject.getString("status");

            // Processamento
            pix = pixService.getDadosPix(pixQrCodeId);

            if (pixQrCodeId != null && pix.size() > 0
                    && pix.get(0).getTransactionId().equals(pixQrCodeId)
                    && pix.get(0).getSituation().equals("AGUARDANDO_PAGAMENTO")) {
                pix.get(0).setSituation(status);

                pixService.editar(pix.get(0));

                if (status.equals("RECEIVED")) {
                    try {
                        Contrato contrato = contratoService.buscarPorId(Long.parseLong(pix.get(0).getContract()));
                        Pagamento pagamento = new Pagamento();
                        pagamento.setTipoDePagamento(pix.get(0).getTypeOfPayment());
                        pagamento.setContrato(contrato);
                        pagamento.setDataPagamento(LocalDate.now());
                        pagamento.setDescricao("Pagamento de aluguel referente a parcela N°" + pix.get(0).getParcel() + ", Vencimento em " + pix.get(0).getDueDate());
                        pagamento.setTipoDePagamento("ENTRADA");
                        pagamento.setTipoDeDocumento("ALUGUEL");
                        pagamento.setUsuario("Thiago Batista Garcia");
                        pagamento.setValorTotal(pix.get(0).getAmount());
                        pagamento.setValorRecebido(pix.get(0).getAmount());
                        pagamento.setValorTotalLiquido(pix.get(0).getAmount());
                        pagamento.setValor(pix.get(0).getAmount());
                        pagamento.setJuros(pix.get(0).getInterestValue());
                        pagamento.setLocador(contrato.getLocador());
                        pagamento.setDesconto(pix.get(0).getDiscount());
                        pagamento.setLocatario(contrato.getLocatario());
                        pagamento.setMeioPagamento("PIX");
                        pagamento.setParcela(new BigDecimal(pix.get(0).getParcel()));
                        pagamento.setTransactionId(pix.get(0).getTransactionId());
                        pagamento.setDataVencimento(pix.get(0).getDueDate());
                        pagamentoService.salvar(pagamento);

                        List<Parcelas> parcelas = parcelaService.getParcela(pagamento.getContrato().getId(), pagamento.getParcela().toString());
                        if (parcelas.isEmpty()) {
                            throw new IllegalStateException("Nenhuma parcela encontrada para o contrato e parcela especificados.");
                        }
                        parcelas.get(0).setSituacao("RECEBIDA");
                        parcelas.get(0).setDataPagamento(LocalDate.now());
                        parcelas.get(0).setCodigoPagamento(pagamento.getId().toString());
                        parcelaService.editar(parcelas.get(0));

                        //log da atividade
                        AtividadeRecente atividadeRecente = new AtividadeRecente();
                        atividadeRecente.setTipoAtividade("RECEBIMENTO_DE_PIX");
                        atividadeRecente.setDataCriacao(LocalDateTime.now());
                        atividadeRecente.setAtividade("Pagamento Pix Confirmado! Chave pix N°:" + pix.get(0).getTransactionId() +
                                ", pagamento N°:" + pagamento.getId() +
                                ", contrato N°:" + pagamento.getContrato().getId() +
                                ", Cliente: " + pagamento.getContrato().getLocatario().getNome() +
                                ", Imovel N°:" + pagamento.getContrato().getImovel().getNumero() +
                                ", referente parcela N°: " + pagamento.getParcela() +
                                ", valor R$" + pagamento.getValorTotalLiquido() +
                                ", data pagamento: " + pagamento.getDataPagamento());
                        atividadeRecenteService.salvar(atividadeRecente);

                        // 🚀 **Envia a atualização via WebSocket**
                        CardUpdate updateInfo = new CardUpdate(contrato.getId(), "Pagamento recebido! " +
                                "Aluguel imóvel: " + pagamento.getContrato().getImovel().getNumero() +
                                ", Locatário: " + pagamento.getContrato().getLocatario().getNome().toUpperCase() +
                                ", Valor R$: " + pagamento.getValorTotalLiquido() +
                                ", chave Pix: " + pix.get(0).getTransactionId(),
                                "sucesso",
                                pix.get(0).getContract());
                        webSocketController.sendCardUpdate(updateInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/dados-pix/{transactionId}")
    public ResponseEntity<?> getDadosPix(@PathVariable("transactionId") String transactionId) {
        List<Pix> dadospix = pixService.getDadosPix(transactionId);
        // 🚀 **Envia a atualização via WebSocket**
        CardUpdate updateInfo = new CardUpdate(dadospix.get(0).getTransactionId(), "Teste Dados de Pix!", "sucesso", dadospix.get(0).getContract());
        webSocketController.sendCardUpdate(updateInfo);
        return ResponseEntity.ok(dadospix);
    }
}
