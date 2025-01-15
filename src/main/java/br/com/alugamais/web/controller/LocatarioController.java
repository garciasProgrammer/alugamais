package br.com.alugamais.web.controller;

import br.com.alugamais.service.LocatarioService;
import br.com.alugamais.web.domain.Locatario;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(("/locatario"))
public class LocatarioController {

    @Autowired
    private LocatarioService locatarioService;

    @GetMapping("/cadastrar")
    public String cadastrar(Locatario locatario) {
        return "locatario/cadastro";
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        Locatario locatario = locatarioService.buscarPorId(id);
        locatario.setNome(locatario.getNome().toUpperCase());
        model.addAttribute("locatario", locatario);
        return "locatario/cadastro";
    }

    @PostMapping("/editar")
    public String editar(@Valid Locatario locatario, BindingResult result, RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "locatario/cadastro";
        }

        locatarioService.editar(locatario);
        attr.addFlashAttribute("success", "Locatário editado com sucesso!");
        return "redirect:/locatario/cadastrar";

    }

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        List<Locatario> clientes = locatarioService.buscarTodos();
        List<Locatario> listaCLiente = new ArrayList<>();
        for (Locatario loc : clientes) {
            loc.setNome(loc.getNome().toUpperCase());
            listaCLiente.add(loc);
        }
        model.addAttribute("locatarios", listaCLiente);
        return "locatario/lista";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Locatario locatario, BindingResult result, RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "locatario/cadastro";
        }

        locatarioService.salvar(locatario);
        attr.addFlashAttribute("success", "Locatário inserido com sucesso!");
        return "redirect:/locatario/cadastrar";
    }
}
