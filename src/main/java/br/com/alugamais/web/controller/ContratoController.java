package br.com.alugamais.web.controller;

import br.com.alugamais.service.ContratoService;
import br.com.alugamais.service.LocadorService;
import br.com.alugamais.service.LocatarioService;
import br.com.alugamais.web.domain.Contrato;
import br.com.alugamais.web.domain.Locador;
import br.com.alugamais.web.domain.Locatario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/contrato")
public class ContratoController {

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private LocadorService locadorService;

    @Autowired
    private LocatarioService locatarioService;

    @GetMapping("/cadastrar")
    public String cadastrar(Contrato contrato) {
        return "contrato/cadastro";
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("contrato", contratoService.buscarPorId(id));
        return "contrato/cadastro";
    }

    @PostMapping("/editar")
    public String editar(@Valid Contrato contrato, BindingResult result, RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "contrato/cadastro";
        }

        contratoService.editar(contrato);
        attr.addFlashAttribute("success", "contrato editado com sucesso!");
        return "redirect:/contrato/cadastrar";

    }

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        model.addAttribute("contratos", contratoService.buscarTodos());
        return "contrato/lista";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Contrato contrato, BindingResult result, RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "contrato/cadastro";
        }

        contratoService.salvar(contrato);
        attr.addFlashAttribute("success", "contrato inserido com sucesso!");
        return "redirect:/contrato/cadastrar";
    }

    @ModelAttribute("locadores")
    public List<Locador> getLocadores() {
        return locadorService.buscarTodos();
    }

    @ModelAttribute("locatarios")
    public List<Locatario> getLocatarios() {
        return locatarioService.buscarTodos();
    }
}
