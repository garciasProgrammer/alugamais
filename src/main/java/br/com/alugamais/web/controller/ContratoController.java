package br.com.alugamais.web.controller;

import br.com.alugamais.dto.ParcelaDTO;
import br.com.alugamais.service.*;
import br.com.alugamais.web.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/contrato")
public class ContratoController {

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private LocadorService locadorService;

    @Autowired
    private LocatarioService locatarioService;

    @Autowired
    private  ParcelaService parcelaService;

    @Autowired
    private ImovelService imovelService;

    @Autowired
    private AtividadeRecenteService atividadeRecenteService;

    @GetMapping("/cadastrar")
    public String cadastrar(Contrato contrato) {
        contrato.setSituacao("ABERTO");
        contrato.setDataAssinatura(LocalDate.now());
        return "contrato/cadastro";
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        Contrato contrato = contratoService.buscarPorId(id);
        model.addAttribute("contrato", contrato);
        return "contrato/cadastro";
    }

    @PostMapping("/editar")
    public String editar(@Valid Contrato contrato, BindingResult result, RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "contrato/cadastro";
        }
        Contrato contratoOld = contratoService.buscarPorId(contrato.getId());
        if(contratoOld.getImovel().getId() != contrato.getImovel().getId()){
            contratoOld.getImovel().setSituacao("Livre");
            imovelService.editar(contratoOld.getImovel());
            contrato.getImovel().setSituacao("Alugado");
            imovelService.editar(contrato.getImovel());
        }
        contratoService.editar(contrato);
        attr.addFlashAttribute("success", "contrato editado com sucesso!");
        return "redirect:/contrato/cadastrar";

    }

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        List<Contrato> contrato = contratoService.buscarTodos();
        model.addAttribute("contratos", contrato);
        return "contrato/lista";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Contrato contrato, @RequestParam("parcelasHidden") String parcelasJson ,BindingResult result, RedirectAttributes attr) throws JsonProcessingException {

        if (result.hasErrors()) {
            attr.addFlashAttribute("fail", "Erro ao salvar contrato. Revise os dados!");
            return "contrato/cadastro";
        }

        //salva o contrato
        contratoService.salvar(contrato);
        //atualiza situação de imovel para alugado
        contrato.getImovel().setSituacao("Alugado");
        imovelService.editar(contrato.getImovel());
        ObjectMapper objectMapper = new ObjectMapper();
        if(!parcelasJson.isEmpty()){
            List<ParcelaDTO> parcelasList = objectMapper.readValue(parcelasJson, new TypeReference<List<ParcelaDTO>>() {});
            // Preencha os dados adicionais e salve as parcelas no banco
            List<Parcelas> parcelas = parcelasList.stream().map(dto -> {
                Parcelas parcela = new Parcelas();
                parcela.setParcela(dto.getParcela());
                parcela.setDataVencimento(LocalDate.parse(dto.getDataVencimento()));
                parcela.setValorAluguel(dto.getValorAluguel());
                parcela.setContrato(contrato);
                // Defina os campos adicionais aqui (exemplo)
                parcela.setSituacao("ABERTA"); // Campo adicional

                return parcela;
            }).collect(Collectors.toList());
            parcelaService.saveAll(parcelas);

            //log da atividade
            AtividadeRecente atividadeRecente = new AtividadeRecente();
            atividadeRecente.setTipoAtividade("CRIACAO_DE_CONTRATO");
            atividadeRecente.setDataCriacao(LocalDateTime.now());
            atividadeRecente.setAtividade("Contrato criado, N°:"+contrato.getId()+", Locatário: "+contrato.getLocatario().getNome()+", data criação: "+ LocalDate.now());
            atividadeRecenteService.salvar(atividadeRecente);

            attr.addFlashAttribute("success", "contrato inserido com sucesso!");
            return "redirect:/contrato/cadastrar";
        }else{
            attr.addFlashAttribute("fail", "Erro ao salvar contrato. Revise os dados!");
            return "contrato/cadastro";
        }

    }

    @ModelAttribute("locadores")
    public List<Locador> getLocadores() {
        return locadorService.buscarTodos();
    }

    @ModelAttribute("locatarios")
    public List<Locatario> getLocatarios() {
        return locatarioService.buscarTodos();
    }

    @ModelAttribute("imoveis")
    public List<Imovel> getImoveis() {
        return imovelService.buscarTodos();
    }
}
