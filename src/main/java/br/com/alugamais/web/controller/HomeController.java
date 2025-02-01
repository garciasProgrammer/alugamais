package br.com.alugamais.web.controller;

import br.com.alugamais.service.AtividadeRecenteService;
import br.com.alugamais.service.ImovelService;
import br.com.alugamais.service.LocatarioService;
import br.com.alugamais.service.PagamentoService;
import br.com.alugamais.web.domain.AtividadeRecente;
import br.com.alugamais.web.domain.Pagamento;
import br.com.alugamais.web.util.ConversorDeMoedaParaBigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    ImovelService imovelService;

    @Autowired
    LocatarioService locatarioService;

    @Autowired
    PagamentoService pagamentoService;

    @Autowired
    AtividadeRecenteService atividadeRecenteService;

    @GetMapping("/home")
    public String home(HttpServletRequest request, ModelMap model) {

        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        double porcentagem = Double.parseDouble(imovelService.getImoveisAlugadosPorcentagem().toString());
        int ativos = Integer.parseInt(imovelService.getImoveisALugados().toString());
        int clientes = Integer.parseInt(locatarioService.getClientes().toString());
        BigDecimal valorMensal = (BigDecimal) pagamentoService.getPagamentosAnoMes();
        List<Pagamento> listPgtos =  pagamentoService.getPagamentosRecebidos();
        List<AtividadeRecente> atividades = atividadeRecenteService.getAtividades();

        model.addAttribute("imovelAtivo", ativos);
        model.addAttribute("porcentagemAtivo", String.format("%.2f%%", porcentagem));
        model.addAttribute("clientes", clientes);
        model.addAttribute("valorMensal", formatoMoeda.format(valorMensal));
        model.addAttribute("pagamentos",listPgtos);
        model.addAttribute("dataHora", LocalDateTime.now());
        model.addAttribute("atividades", atividades);

        return "home";
    }

}
