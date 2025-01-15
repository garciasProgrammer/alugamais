package br.com.alugamais.web.controller;

import br.com.alugamais.service.LocadorService;
import br.com.alugamais.service.LocadorService;
import br.com.alugamais.web.domain.Locador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(("/locador"))
public class LocadorController {

    @Autowired
    private LocadorService locadorService;

    @GetMapping("/cadastrar")
    public String cadastrar(Locador locador) {
        return "locador/cadastro";
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("locador", locadorService.buscarPorId(id));
        return "locador/cadastro";
    }

    @PostMapping("/editar")
    public String editar(@Valid Locador locador, BindingResult result, RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "locador/cadastro";
        }

        locadorService.editar(locador);
        attr.addFlashAttribute("success", "Locador editado com sucesso!");
        return "redirect:/locador/cadastrar";

    }

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        model.addAttribute("locadores", locadorService.buscarTodos());
        return "locador/lista";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Locador locador, BindingResult result, RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "locador/cadastro";
        }

        locadorService.salvar(locador);
        attr.addFlashAttribute("success", "Locador inserido com sucesso!");
        return "redirect:/locador/cadastrar";
    }

    @ModelAttribute("locadores")
    public List<Locador> getLocadores() {
        return locadorService.buscarTodos();
    }
}
