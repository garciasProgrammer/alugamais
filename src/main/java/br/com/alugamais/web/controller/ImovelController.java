package br.com.alugamais.web.controller;

import br.com.alugamais.service.ImovelService;
import br.com.alugamais.service.LocadorService;
import br.com.alugamais.web.domain.Imovel;
import br.com.alugamais.web.domain.Locador;
import br.com.alugamais.web.util.ConversorDeMoedaParaBigDecimal;
import br.com.alugamais.web.util.ConverteStringToBigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/imovel")
public class ImovelController {

    @Autowired
    private ImovelService imovelService;

    @Autowired
    LocadorService locadorService;

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
        return "redirect:/imovel/cadastrar";

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
        return "redirect:/imovel/cadastrar";
    }

    @ModelAttribute("locadores")
    public List<Locador> getLocadores() {
        return locadorService.buscarTodos();
    }

    @GetMapping("/livres/lista/{locadorId}")
    public ResponseEntity<?> getImoveisPorlocador(@PathVariable("locadorId") Long locadorId, ModelMap model) {
        List<Imovel> imoveis = imovelService.getImoveisPorLocador("Livre", locadorId);
        return ResponseEntity.ok(imoveis);
    }

    @GetMapping("/detalhes/{imovelId}")
    public ResponseEntity<?> getDetalhesImovel(@PathVariable("imovelId") Long imovelId, ModelMap model) {
        Imovel imovel = imovelService.buscarPorId(imovelId);
        return ResponseEntity.ok(imovel);
    }
}
