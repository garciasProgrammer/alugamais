package br.com.alugamais.web.controller;

import br.com.alugamais.service.PagamentoService;
import br.com.alugamais.service.PagamentoService;
import br.com.alugamais.web.domain.Pagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

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
        return "redirect:/pagamentos/cadastrar";

    }

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        model.addAttribute("pagamentos", pagamentoService.buscarTodos());
        return "pagamento/lista";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Pagamento pagamento, BindingResult result, RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "pagamento/cadastro";
        }

        pagamentoService.salvar(pagamento);
        attr.addFlashAttribute("success", "Pagamento inserido com sucesso!");
        return "redirect:/pagamentos/cadastrar";
    }
}
