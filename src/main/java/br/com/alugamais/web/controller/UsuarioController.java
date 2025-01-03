package br.com.alugamais.web.controller;


import br.com.alugamais.dto.AlterarSenhaDTO;
import br.com.alugamais.dto.UserInfoDTO;
import br.com.alugamais.service.EmailService;
import br.com.alugamais.service.UsuarioService;
import br.com.alugamais.web.domain.Email;
import br.com.alugamais.web.domain.Usuario;
import br.com.alugamais.web.enums.StatusEmail;
import br.com.alugamais.web.util.CriptpgrafarSenhas;
import br.com.alugamais.web.util.GeradorDeSenhaAleatoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    CriptpgrafarSenhas cripotografiaDeSenha = new CriptpgrafarSenhas();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @GetMapping("/cadastrar")
    public String cadastrar(Usuario usuario) {

        return "usuario/cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, RedirectAttributes attr) {

        String senhaInicial = GeradorDeSenhaAleatoria.senhaInicial();
        usuario.setPassword(cripotografiaDeSenha.encodeSenha(senhaInicial));

        if (result.hasErrors()) {

            return "usuario/cadastro";
        }

        usuarioService.salvar(usuario);
        enviaEmailComSenhaInicial(usuario.getEmail(), usuario.getNome().toUpperCase(), usuario.getUserName(), senhaInicial);
        attr.addFlashAttribute("success", "Usuário inserido com sucesso. Um E-mail com a sua senha inicial foi enviado para: " + usuario.getEmail());

        return "redirect:/usuarios/cadastrar";
    }

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        model.addAttribute("usuarios", usuarioService.buscarTodos());
        return "usuario/lista";

    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("usuario", usuarioService.buscarPorId(id));
        return "usuario/cadastro";
    }

    @PostMapping("/editar")
    public String editar(@ModelAttribute("usuario") Usuario usuario, BindingResult result, RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "usuario/cadastro";
        }

        Usuario usuarioExiste = usuarioService.buscarPorId(usuario.getId());

        if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            usuario.setPassword(usuarioExiste.getPassword());
        }

        usuarioService.editar(usuario);
        attr.addFlashAttribute("success", "Usuário editado com sucesso.");
        return "redirect:/usuarios/cadastrar";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, @ModelAttribute("userInfo") UserInfoDTO userInfoDTO, RedirectAttributes attr) {

        if (userInfoDTO.getUsuarioLogadoId().equals(id)) {
            attr.addFlashAttribute("error", "Não é permitido remover um Usuário logado.");
        } else {
            usuarioService.excluir(id);
            attr.addFlashAttribute("success", "Usuário removido com sucesso.");
        }
        return "redirect:/usuarios/listar";
    }


    public void enviaEmailComSenhaInicial(String getEmail, String nome, String nomeUsuario, String senhaInicial) {
        Email email = new Email();
        email.setEmailFrom("access-manager@fleetcarservice.com.br");
        email.setEmailTo(getEmail);
        email.setSubject("BEM VINDO AO FLEETCAR SERVICE");
        email.setText("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "<style>\n" +
                "  .container {\n" +
                "    position: relative; \n" + /* Posicionamento relativo para o contêiner */
                "    width: 1125px; \n" + /* Pode ser ajustado conforme necessário */
                "    height: 750px; \n" + /* Ou um valor fixo se você tiver um tamanho específico em mente */
                "    background: url('cid:backgroundImage') no-repeat center center;\n" +  /* Imagem centralizada */
                "    background-size: 80%; \n" + /* Imagem cobre todo o contêiner */
                "  }\n" +
                "  .content {\n" +
                "    position: absolute;\n" +  /* Posicionamento absoluto para o texto */
                "    top: 70%; \n" + /* Posiciona o texto a 70% abaixo do topo do contêiner */
                "    width: 100%; \n" + /* A .content ocupa a largura total */
                "    text-align: center; \n" + /* Centraliza o texto dentro da .content */
                "  }\n" +
                "  .text-style {\n" +
                "    color: blue; \n" + /* Estilização do texto */
                "    font-weight: bold; \n" + /* Texto em negrito */
                "    font-size: 18px; \n" +
                "  }\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"container\">\n" +
                "    <div class=\"content\">\n" +
                "    <br><br><br><br><br><br><br><br><br><br>" +
                "    <br><br><br><br><br><br><br><br><br><br>\n" +
                "      <div class=\"text-style\">Bem vindo, " + nome + "!</div>\n" +
                "      <div class=\"text-style\">Usuário: " + nomeUsuario + "</div>\n" +
                "      <div class=\"text-style\">Sua senha é: " + senhaInicial + "</div>\n" +
                "      <br><br><br><br><br>\n" +
                "      <div class=\"text-style\">FLEETCAR SERVICE</div>\n" +
                "      <br><br><br><br><br><br><br><br><br><br>" +
                "      <br><br><br><br><br><br><br><br><br><br>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>\n");
        email.setStatusEmail(StatusEmail.SENT);
        email.setSendDateEmail(LocalDateTime.now());
        try {
            emailService.enviarEmailHtml(email.getEmailTo(), email.getSubject(), email.getText());
            emailService.salvar(email);
        } catch (Exception e) {
            email.setStatusEmail(StatusEmail.ERROR);
            emailService.salvar(email);
            e.printStackTrace();
        }
    }

    @GetMapping("/senha/{id}")
    public String preEditarSenha(@PathVariable("id") Long id, ModelMap model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        AlterarSenhaDTO alt = new AlterarSenhaDTO();
        alt.setId(usuario.getId());
        alt.setNome(usuario.getNome());
        alt.setUserName(usuario.getUserName());

        model.addAttribute("alterarSenhaDTO", alt);
        return "usuario/editar-senha";
    }

    @PostMapping("/senha")
    public String editarSenha(@ModelAttribute("alterarSenha") AlterarSenhaDTO alterarSenhaDTO, BindingResult result, RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "usuario/cadastro";
        }


        Usuario usuarioExiste = usuarioService.buscarPorId(alterarSenhaDTO.getId());
        if (!passwordEncoder.matches(alterarSenhaDTO.getSenhaAtual(), usuarioExiste.getPassword())) {
            attr.addFlashAttribute("fail", "Senha atual não confere.");
            return "redirect:/usuarios/senha/" + alterarSenhaDTO.getId();
        }
        if (!alterarSenhaDTO.getNovaSenha().equals(alterarSenhaDTO.getConfirmaSenha())) {
            attr.addFlashAttribute("fail", "A Nova é senha não é igual a confirmação.");
            return "redirect:/usuarios/senha/" + alterarSenhaDTO.getId();
        }

        usuarioService.updatePassword(alterarSenhaDTO.getId(), cripotografiaDeSenha.encodeSenha(alterarSenhaDTO.getNovaSenha()));
        attr.addFlashAttribute("success", "Usuário editado com sucesso.");
        return "redirect:/usuarios/cadastrar";
    }

    @GetMapping("/recuperar-senha")
    public String recuperarSenha(@RequestParam("email") String email, RedirectAttributes attr) {

        Usuario usuario = usuarioService.findByEmail(email);
        if (usuario == null) {
            attr.addFlashAttribute("error", "Nenhum usuário encontrado com esse e-mail.");
            return "redirect:/recuperar-senha";
        }

        String senhaInicial = GeradorDeSenhaAleatoria.senhaInicial();
        usuario.setPassword(cripotografiaDeSenha.encodeSenha(senhaInicial));
        usuarioService.editar(usuario);
        enviaEmailComSenhaInicial(usuario.getEmail(), usuario.getNome().toUpperCase(), usuario.getUserName(), senhaInicial);
        attr.addFlashAttribute("success", "Senha reenviada com sucesso");

        return "redirect:/recuperar-senha";
    }

}
