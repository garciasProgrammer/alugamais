package br.com.alugamais.web.controller;

import br.com.alugamais.service.ImovelService;
import br.com.alugamais.web.domain.Imovel;
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
@RequestMapping("/imoveis")
public class ImovelController {

    @Autowired
    private ImovelService imovelService;

    @GetMapping("/cadastrar")
    public String cadastrar(Imovel imovel) {
        return "imovel/cadastro";
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("imovel", imovelService.buscarPorId(id));
        return "imovel/cadastro";
    }

    @PostMapping("/editar")
    public String editar(@Valid Imovel imovel, BindingResult result, RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "imovel/cadastro";
        }

        imovelService.editar(imovel);
        attr.addFlashAttribute("success", "Imóvel editado com sucesso!");
        return "redirect:/imoveis/cadastrar";

    }

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        model.addAttribute("imoveis", imovelService.buscarTodos());
        return "imovel/lista";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Imovel imovel, BindingResult result, RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "imovel/cadastro";
        }

        imovelService.salvar(imovel);
        attr.addFlashAttribute("success", "Imóvel inserido com sucesso!");
        return "redirect:/imoveis/cadastrar";
    }
}
