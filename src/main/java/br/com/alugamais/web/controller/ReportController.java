package br.com.alugamais.web.controller;

import br.com.alugamais.service.*;

import br.com.alugamais.web.config.hibernate.TenantContext;
import br.com.alugamais.web.domain.Contrato;
import br.com.alugamais.web.domain.Pagamento;
import br.com.alugamais.web.enums.MensagemWhatsApp;
import br.com.alugamais.web.util.ConverteValorParaExtenso;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @Autowired
    private PagamentoService pagamentoService;

    @Autowired
    private ContratoService contratoService;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    WhatsAppService whatsAppService;

    @Autowired
    private LocadorService locadorService;


    @Autowired
    private ParcelaService parcelaService;


    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));

    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    DateTimeFormatter formatoDataNormal = DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("pt", "BR"));

    NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance();

    @Autowired
    ContratoController contratoController;

    @GetMapping("/Recibo/Geral/{codigoPagamento}/{tipoDocumento}")
    @ResponseBody
    public ResponseEntity<byte[]> getReportReciboGeral(@PathVariable("tipoDocumento") String tipoDocumento, @PathVariable("codigoPagamento") String codigoPagamento) throws Exception {
        return gerarReciboGeral(tipoDocumento, codigoPagamento);
    }

    @GetMapping("/Recibo/Whats/Geral/{codigoPagamento}/{tipoDocumento}")
    @ResponseBody
    public void getReportReciboWhatsGeral(@PathVariable("tipoDocumento") String tipoDocumento, @PathVariable("codigoPagamento") String codigoPagamento) throws Exception {
        try {
            Pagamento pgto = pagamentoService.buscarPorId(Long.parseLong(codigoPagamento));
            ResponseEntity<byte[]> resposta = this.gerarReciboGeral(tipoDocumento, codigoPagamento);
            if (resposta.getStatusCode() == HttpStatus.OK) {
                byte[] reportContent = resposta.getBody();
                String base64Content = Base64.getEncoder().encodeToString(reportContent);

                whatsAppService.enviarMensagem(pgto.getContrato().getLocatario().getCelularWhatsApp(),
                        MensagemWhatsApp.MENSAGEM_PAGAMENTO_WHATSAPP.format(pgto.getLocador().getNome().toUpperCase(),
                                pgto.getContrato().getId(),
                                pgto.getContrato().getImovel().getNumero(),
                                pgto.getContrato().getLocatario().getNome().toUpperCase(),
                                formatoDataNormal.format(pgto.getDataPagamento()),
                                pgto.getParcela(),
                                formatoMoeda.format(pgto.getValorTotalLiquido())));
                whatsAppService.enviarDocumento(pgto.getContrato().getLocatario().getCelularWhatsApp(), base64Content, String.valueOf(pgto.getId()));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/recibo/aluguel/{codigoPagamento}")
    @ResponseBody
    public ResponseEntity<byte[]> getReportReciboDiaria(@PathVariable("codigoPagamento") String codigoPagamento) throws Exception {
        return gerarReciboAluguel(codigoPagamento);
    }

    @GetMapping("/contrato/aluguel/{codigoContrato}")
    @ResponseBody
    public ResponseEntity<byte[]> getReportContratoALuguel(@PathVariable("codigoContrato") String codigoContrato) throws Exception {
        return gerarContratoAluguel(codigoContrato);
    }

    @GetMapping("/recibo/whats/aluguel/{codigoPagamento}")
    @ResponseBody
    public ResponseEntity<String> getReportReciboWhatsDiaria(@PathVariable("codigoPagamento") String codigoPagamento) throws Exception {

        try {
            Pagamento pgto = pagamentoService.buscarPorId(Long.parseLong(codigoPagamento));
            ResponseEntity<byte[]> resposta = this.gerarReciboAluguel(codigoPagamento);

            if (resposta.getStatusCode() == HttpStatus.OK) {
                byte[] reportContent = resposta.getBody();
                String base64Content = Base64.getEncoder().encodeToString(reportContent);

                // Envia mensagens e documentos pelo WhatsApp
                whatsAppService.enviarMensagem(pgto.getContrato().getLocatario().getCelularWhatsApp(),
                        MensagemWhatsApp.MENSAGEM_PAGAMENTO_WHATSAPP.format(pgto.getLocador().getNome().toUpperCase(),
                                pgto.getContrato().getId(),
                                pgto.getContrato().getImovel().getNumero(),
                                pgto.getContrato().getLocatario().getNome().toUpperCase(),
                                formatoDataNormal.format(pgto.getDataPagamento()),
                                pgto.getParcela(),
                                formatoMoeda.format(pgto.getValorTotalLiquido())));
                whatsAppService.enviarDocumento(pgto.getContrato().getLocatario().getCelularWhatsApp(), base64Content, String.valueOf(pgto.getId()));

                whatsAppService.enviarMensagem(pgto.getContrato().getLocador().getCelularLocador(),
                        MensagemWhatsApp.MENSAGEM_PAGAMENTO_WHATSAPP.format(pgto.getLocador().getNome().toUpperCase(),
                                pgto.getContrato().getId(),
                                pgto.getContrato().getImovel().getNumero(),
                                pgto.getContrato().getLocatario().getNome().toUpperCase(),
                                formatoDataNormal.format(pgto.getDataPagamento()),
                                pgto.getParcela(),
                                formatoMoeda.format(pgto.getValorTotalLiquido())));
                whatsAppService.enviarDocumento(pgto.getContrato().getLocatario().getCelularWhatsApp(), base64Content, String.valueOf(pgto.getId()));

                // Retorna sucesso ao frontend
                return ResponseEntity.ok("ok");
            }

            // Retorna erro se o status da resposta não for OK
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao gerar o recibo.");

        } catch (Exception e) {
            e.printStackTrace();
            // Retorna erro ao frontend
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao enviar o recibo via WhatsApp: " + e.getMessage());
        }
    }

    public ResponseEntity<byte[]> gerarReciboGeral(String tipoDocumento, String codigoPagamento) throws Exception {
        String tenant = TenantContext.getCurrentTenant();
        //pega os dados do pgto
        Resource jrxmlResource = resourceLoader.getResource("classpath:/reports/" + tenant + "/ReciboGeral.jrxml");
        InputStream jrxmlInputStream = jrxmlResource.getInputStream();
        Pagamento pagamento = pagamentoService.buscarPorId(Long.valueOf(codigoPagamento));
        Map<String, Object> paramValue = new HashMap<>();
        paramValue.put("codigo_pagamento_principal", String.valueOf(pagamento.getId()));
        paramValue.put("valor_total_liquido_extenso", String.valueOf(new ConverteValorParaExtenso(pagamento.getValorTotalLiquido().compareTo(new BigDecimal(0)) > 0 ? pagamento.getValorTotalLiquido() : pagamento.getValorTotalLiquido().multiply(new BigDecimal(-1)))));
        paramValue.put("SUBREPORT_DIR", "classpath:reports/" + tenant + "/forma_pagamento_sub.jasper");
        paramValue.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
        byte[] reportContent = reportService.generateReport(jrxmlInputStream, paramValue);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Para forçar o download do PDF em vez de exibi-lo no navegador, descomente a linha abaixo
        // headers.setContentDispositionFormData("filename", "Recibo-Deposito-450.pdf");
        return new ResponseEntity<>(reportContent, headers, HttpStatus.OK);
    }

    public ResponseEntity<byte[]> gerarReciboAluguel(String codigoPagamento) throws Exception {
        String tenant = TenantContext.getCurrentTenant();
        InputStream jrxmlInputStream = null;
        Resource jrxmlResource;
        Map<String, Object> paramValue = new HashMap<>();
        InputStream caminhoLogo = getClass().getResourceAsStream("/reports/imagem_geral/" + tenant + "_logo.png");
        //pega os dados do pgto
        Pagamento pagamento = pagamentoService.buscarPorId(Long.valueOf(codigoPagamento));


        jrxmlResource = resourceLoader.getResource("classpath:/reports/" + tenant + "/ReciboAluguel.jrxml");
        jrxmlInputStream = jrxmlResource.getInputStream();
        paramValue.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));

        paramValue.put("codigoPgto", String.valueOf(pagamento.getId()));
        paramValue.put("valorPagoPorExtenso", String.valueOf(new ConverteValorParaExtenso(pagamento.getValorTotalLiquido())));
        paramValue.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
        byte[] reportContent = reportService.generateReport(jrxmlInputStream, paramValue);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Para forçar o download do PDF em vez de exibi-lo no navegador, descomente a linha abaixo
        // headers.setContentDispositionFormData("filename", "Recibo-Deposito-450.pdf");
        // Adiciona o código do pagamento como um cabeçalho personalizado
        headers.add("codigoPagamento", String.valueOf(pagamento.getId()));
        return new ResponseEntity<>(reportContent, headers, HttpStatus.OK);
    }

    public ResponseEntity<byte[]> gerarContratoAluguel(String codigoContrato) throws Exception {
        String tenant = TenantContext.getCurrentTenant();
        InputStream jrxmlInputStream = null;
        Resource jrxmlResource;
        Map<String, Object> paramValue = new HashMap<>();
        //pega os dados do pgto
        Contrato contrato = contratoService.buscarPorId(Long.parseLong(codigoContrato));


        jrxmlResource = resourceLoader.getResource("classpath:/reports/" + tenant + "/ContratoAluguel.jrxml");
        jrxmlInputStream = jrxmlResource.getInputStream();
        paramValue.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));

        paramValue.put("codigoContrato", String.valueOf(contrato.getId()));
        paramValue.put("valorPagoPorExtenso", String.valueOf(new ConverteValorParaExtenso(contrato.getValorAluguel())));
        paramValue.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
        byte[] reportContent = reportService.generateReport(jrxmlInputStream, paramValue);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Para forçar o download do PDF em vez de exibi-lo no navegador, descomente a linha abaixo
        // headers.setContentDispositionFormData("filename", "Recibo-Deposito-450.pdf");
        // Adiciona o código do pagamento como um cabeçalho personalizado
        headers.add("codigoPagamento", String.valueOf(contrato.getId()));
        return new ResponseEntity<>(reportContent, headers, HttpStatus.OK);
    }

    @GetMapping("/Contrato/Cancelado/{codigo}")
    public ResponseEntity<byte[]> getReportDadosCancelamentoContrato(@PathVariable("codigo") String codigo, HttpServletResponse response) throws Exception {
        return gerarDocumentoCancelamentoContrato(codigo);

    }

    @GetMapping("/quitacao/{codigo}")
    public ResponseEntity<byte[]> getReportDadosQuitacaoContrato(@PathVariable("codigo") String codigo, HttpServletResponse response) throws Exception {
        return gerarDocumentoQuitacaoContrato(codigo);

    }


    public ResponseEntity<byte[]> gerarDocumentoQuitacaoContrato(String codigo) throws Exception {
        String tenant = TenantContext.getCurrentTenant();
        InputStream jrxmlInputStream;
        Resource jrxmlResource;
        Map<String, Object> paramValue = new HashMap<>();
        InputStream caminhoLogo = getClass().getResourceAsStream("/reports/imagem_geral/" + tenant + "_logo.png");

        jrxmlResource = resourceLoader.getResource("classpath:/reports/" + tenant + "/quitacao-contrato.jrxml");
        jrxmlInputStream = jrxmlResource.getInputStream();
        paramValue.put("codigo_contrato", codigo);
        paramValue.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
        byte[] reportContent = reportService.generateReport(jrxmlInputStream, paramValue);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Para forçar o download do PDF em vez de exibi-lo no navegador, descomente a linha abaixo
        headers.setContentDispositionFormData("filename", "DOCUMENTO-QUITACAO-CONTRATO-" + codigo + ".pdf");
        return new ResponseEntity<>(reportContent, headers, HttpStatus.OK);
    }

    public ResponseEntity<byte[]> gerarDocumentoCancelamentoContrato(String codigo) throws Exception {
        String tenant = TenantContext.getCurrentTenant();
        InputStream jrxmlInputStream;
        Resource jrxmlResource;
        Map<String, Object> paramValue = new HashMap<>();
        InputStream caminhoLogo = getClass().getResourceAsStream("/reports/imagem_geral/" + tenant + "_logo.png");

        jrxmlResource = resourceLoader.getResource("classpath:/reports/" + tenant + "/cancelamento_contrato.jrxml");
        jrxmlInputStream = jrxmlResource.getInputStream();
        paramValue.put("SUBREPORT_ITENS", "classpath:reports/" + tenant + "/itens_servicos_sub.jasper");
        paramValue.put("logo", caminhoLogo);
        paramValue.put("codigoContrato", codigo);
        paramValue.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
        byte[] reportContent = reportService.generateReport(jrxmlInputStream, paramValue);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Para forçar o download do PDF em vez de exibi-lo no navegador, descomente a linha abaixo
        // headers.setContentDispositionFormData("filename", "Recibo-Deposito-450.pdf");
        return new ResponseEntity<>(reportContent, headers, HttpStatus.OK);
    }

}


