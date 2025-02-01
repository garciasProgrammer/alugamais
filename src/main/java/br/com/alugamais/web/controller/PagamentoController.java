package br.com.alugamais.web.controller;

import br.com.alugamais.service.*;
import br.com.alugamais.service.PagamentoService;
import br.com.alugamais.web.config.security.CustomUserDetails;
import br.com.alugamais.web.domain.*;
import br.com.alugamais.web.util.DataUtil;
import br.com.alugamais.web.util.PixUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pagamento")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private ParcelaService parcelaService;

    @Autowired
    private PixService pixService;

    private PixUtil pixUtil = new PixUtil();

    @Autowired
    private AtividadeRecenteService atividadeRecenteService;

    @GetMapping("/cadastrar")
    public String cadastrar(Pagamento pagamento) {
        return "pagamento/cadastro";
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("pagamento", pagamentoService.buscarPorId(id));
        return "pagamento/cadastro";
    }

    @PostMapping("/editar")
    public String editar(@Valid Pagamento pagamento, BindingResult result, RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "pagamento/cadastro";
        }

        pagamentoService.editar(pagamento);
        attr.addFlashAttribute("success", "Pagamento editado com sucesso!");
        return "redirect:/pagamento/cadastrar";

    }

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        model.addAttribute("pagamentos", pagamentoService.buscarTodos());
        return "pagamento/lista";
    }

    @PostMapping("/salvar")
    public ResponseEntity<Map<String, Object>> salvar(@Valid Pagamento pagamento,
                                                      BindingResult result, RedirectAttributes attr) throws IOException {
        // Retornar ID do contrato como resposta JSON
        Map<String, Object> response = new HashMap<>();

        pagamento.setDescricao("Pagamento de aluguel referente a parcela N°" + pagamento.getParcela() + ", Vencimento em " + pagamento.getDataVencimento());
        pagamento.setDataPagamento(LocalDate.now());
        pagamento.setTipoDePagamento("ENTRADA");
        pagamento.setTipoDeDocumento("ALUGUEL");
        pagamento.setUsuario(CustomUserDetails.getUsuarioLogado());
        pagamento.setValorTotal(pagamento.getValor());
        pagamento.setTransactionId("");
        pagamento.setValorRecebido(pagamento.getValorTotalLiquido());
        if (pagamento.getDesconto() == null) {
            pagamento.setDesconto(new BigDecimal(0.00));
        }

        if (pagamento.isAction()) {
            pagamentoService.salvar(pagamento);
            List<Parcelas> parcelas = parcelaService.getParcela(pagamento.getContrato().getId(), pagamento.getParcela().toString());
            if (parcelas.isEmpty()) {
                throw new IllegalStateException("Nenhuma parcela encontrada para o contrato e parcela especificados.");
            }
            parcelas.get(0).setSituacao("RECEBIDA");
            parcelas.get(0).setDataPagamento(LocalDate.now());
            parcelas.get(0).setCodigoPagamento(pagamento.getId().toString());
            parcelaService.editar(parcelas.get(0));
            attr.addFlashAttribute("success", "Pagamento inserido com sucesso!");

            //log da atividade
            AtividadeRecente atividadeRecente = new AtividadeRecente();
            atividadeRecente.setTipoAtividade("PAGAMENTO_COMUM");
            atividadeRecente.setDataCriacao(LocalDateTime.now());
            atividadeRecente.setAtividade("Pagamento comum (Boleto, transferência, em espécie) N°:" + pagamento.getId() +
                    ", contrato N°:" + pagamento.getContrato().getId() +
                    ", Cliente: " + pagamento.getContrato().getLocatario().getNome() +
                    ", Imovel N°:" + pagamento.getContrato().getImovel().getNumero() +
                    ", referente parcela N°: " + pagamento.getParcela() +
                    ", valor pago R$" + pagamento.getValorTotalLiquido() +
                    ", data pagamento: " + pagamento.getDataPagamento());
            atividadeRecenteService.salvar(atividadeRecente);

            response.put("isAction", true);
            response.put("success", true);
            response.put("contratoId", pagamento.getContrato().getId());
        } else {


            LocalDateTime dateExpiration = dataExpiracaoChavePix();
            List<Parcelas> parcelasList = parcelaService.getParcela(pagamento.getContrato().getId(), pagamento.getParcela().toString());
            Response responsePix = pixUtil.getPixKey(pagamento.getValorTotalLiquido(), dateExpiration);

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
                pix.setUser(CustomUserDetails.getUsuarioLogado());
                pix.setFeeValue(new BigDecimal(0.00));
                pix.setInterestValue(pagamento.getJuros());
                pix.setPaymentMethod(pagamento.getMeioPagamento());
                pix.setTypeOfPayment("ENTRADA");
                pix.setTypeOfDocument("ALUGUEL");
                pix.setDueDate(parcelasList.get(0).getDataVencimento());
                pixService.salvar(pix);

                //log da atividade
                AtividadeRecente atividadeRecente = new AtividadeRecente();
                atividadeRecente.setTipoAtividade("CRIA_CHAVE_PIX");
                atividadeRecente.setDataCriacao(LocalDateTime.now());
                atividadeRecente.setAtividade("Pedido de chave pix N°:" + pix.getTransactionId() +
                        ", contrato N°:" + pagamento.getContrato().getId() +
                        ", Cliente: " + pagamento.getContrato().getLocatario().getNome() +
                        ", Imovel N°:" + pagamento.getContrato().getImovel().getNumero() +
                        ", referente parcela N°: " + pagamento.getParcela() +
                        ", valor R$" + pagamento.getValorTotalLiquido() +
                        ", data pedido: " + LocalDateTime.now());
                atividadeRecenteService.salvar(atividadeRecente);

                // Retorna as informações da chave pix
                response.put("isAction", false);
                response.put("qrCode", qrCode);
                response.put("copiaCola", copiaCola);
                response.put("contratoId", pagamento.getContrato().getId());
                response.put("valorPix", pagamento.getValorTotalLiquido());
                response.put("success", true);
            } else {
                response.put("success", false);
            }

        }

        return ResponseEntity.ok(response);
    }


    @PostMapping("/estornar/{codigoPagamento}")
    public ResponseEntity<Map<String, Object>> estornarPagamento(@PathVariable Long codigoPagamento) {
        Map<String, Object> response = new HashMap<>();
        try {
            Pagamento pagamento = pagamentoService.buscarPorId(codigoPagamento);
            pagamento.setDataCancelado(LocalDate.now());
            pagamento.setUsuarioCancelador("Thiago Batista Garcia");
            pagamento.setTipoDePagamento("ESTORNO");
            pagamento.setValorTotalLiquido(pagamento.getValorTotalLiquido().multiply(new BigDecimal(-1)));
            pagamentoService.editar(pagamento);

            List<Parcelas> parcelas = parcelaService.getParcela(pagamento.getContrato().getId(), String.valueOf(pagamento.getParcela().intValue()));
            parcelas.get(0).setSituacao("ABERTA");
            parcelas.get(0).setCodigoPagamento(null);
            parcelas.get(0).setDataPagamento(null);
            parcelaService.editar(parcelas.get(0));

            //log da atividade
            AtividadeRecente atividadeRecente = new AtividadeRecente();
            atividadeRecente.setTipoAtividade("ESTORNO_PAGAMENTO");
            atividadeRecente.setDataCriacao(LocalDateTime.now());
            atividadeRecente.setAtividade("Pagamento Estornado N°:" + pagamento.getId() +
                    ", contrato N°:" + pagamento.getContrato().getId() +
                    ", Cliente: " + pagamento.getContrato().getLocatario().getNome() +
                    ", Imovel N°:" + pagamento.getContrato().getImovel().getNumero() +
                    ", referente parcela N°: " + pagamento.getParcela() +
                    ", valor pago R$" + pagamento.getValorTotalLiquido() +
                    ", data pagamento: " + pagamento.getDataPagamento());
            atividadeRecenteService.salvar(atividadeRecente);

            // Retornar resultado do estorno em JSON
            response.put("success", true);
            response.put("message", "Pagamento estornado com sucesso!");
            response.put("contratoId", pagamento.getContrato().getId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {

            response.put("success", false);
            response.put("message", "Erro ao estornar pagamento. Erro: \n" + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/extrato-financeiro/{contratoId}")
    @ResponseBody
    public Map<String, Object> getExtratoFinanceiro(@PathVariable("contratoId") Long contratoId) {
        Map<String, Object> response = new HashMap<>();
        response.put("contrato", contratoService.buscarPorId(contratoId));
        response.put("parcelas", parcelaService.buscaParcelas(contratoId));
        return response;
    }


    @GetMapping("/dados-parcela/{contratoId}/parcela/{numParcela}")
    @ResponseBody
    public Map<String, Object> getDadosParcela(@PathVariable("contratoId") Long contratoId, @PathVariable("numParcela") String numParcela) {
        List<Parcelas> parcela = parcelaService.getParcela(contratoId, numParcela);
        Map valores = calculaValoresDoAluguel(parcela.get(0));
        Map<String, Object> response = new HashMap<>();
        response.put("juros", valores.get("juros"));
        response.put("valorAluguel", valores.get("valorAluguel"));
        response.put("taxaCond", valores.get("taxaCond"));
        response.put("parcela", parcela.get(0).getParcela());
        response.put("diasEmAtraso", valores.get("diasEmAtraso"));
        response.put("dataVencimento", parcela.get(0).getDataVencimento());
        response.put("locadorPagamento", parcela.get(0).getContrato().getLocador());
        response.put("locatarioPagamento", parcela.get(0).getContrato().getLocatario());
        response.put("contratoPagamento", parcela.get(0).getContrato());
        return response;
    }

    @ModelAttribute("contratos")
    public List<Contrato> getContratos(ModelMap model) {
        List<Contrato> contratos = contratoService.buscarTodos();
        return contratos;
    }

    // Converter java.util.Date ou java.sql.Date para String no formato dd/MM/yyyy
    public static String convertDateToString(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    // Converter LocalDate para String no formato dd/MM/yyyy
    public static String convertLocalDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private Map calculaValoresDoAluguel(Parcelas parcelas) {
        Map<String, Object> valores = new HashMap<>();
        BigDecimal valorAluguel = parcelas.getValorAluguel();
        BigDecimal juros = new BigDecimal(0.00);
        BigDecimal taxaCondominio = parcelas.getContrato().getImovel().getValorTaxa();
        int dias = 0;

        if (parcelas.getDataVencimento().isBefore(LocalDate.now())) {
            //cobrando 2% a.d do valor original do titulo
            String dataInicial = convertLocalDateToString(parcelas.getDataVencimento());
            String dataFinal = convertLocalDateToString(LocalDate.now());
            DataUtil dataUtil = new DataUtil();
            dias = dataUtil.obtemQuantidadeDeDiasEntreDatas(dataInicial, dataFinal);
            juros = (parcelas.getValorAluguel().multiply(new BigDecimal(0.02))).multiply(new BigDecimal(dias));
        }
        valores.put("juros", juros);
        valores.put("valorAluguel", valorAluguel);
        valores.put("taxaCond", taxaCondominio);
        valores.put("diasEmAtraso", dias);

        return valores;
    }

    private LocalDateTime dataExpiracaoChavePix() {
        LocalDateTime now = LocalDateTime.now();
        return now.plusMinutes(30);
    }


}
