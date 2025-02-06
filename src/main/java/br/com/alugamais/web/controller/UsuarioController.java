package br.com.alugamais.web.controller;


import br.com.alugamais.dto.AlterarSenhaDTO;
import br.com.alugamais.dto.UserInfoDTO;
import br.com.alugamais.service.EmailService;
import br.com.alugamais.service.UsuarioService;
import br.com.alugamais.web.config.security.CustomUserDetails;
import br.com.alugamais.web.domain.Email;
import br.com.alugamais.web.domain.Usuario;
import br.com.alugamais.web.enums.StatusEmail;
import br.com.alugamais.web.util.CriptpgrafarSenhas;
import br.com.alugamais.web.util.GeradorDeSenhaAleatoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    CriptpgrafarSenhas cripotografiaDeSenha = new CriptpgrafarSenhas();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @GetMapping("/perfil")
    public String perfil(Usuario usuario, ModelMap model) {

        String usuarioLogado = CustomUserDetails.getUsuarioLogado();
        Usuario usuarioSistema = usuarioService.buscarPorUserName(usuarioLogado);
        model.addAttribute("usuario", usuarioSistema);

        AlterarSenhaDTO alterarSenhaDTO = new AlterarSenhaDTO();
        alterarSenhaDTO.setId(usuarioSistema.getId());
        alterarSenhaDTO.setNome(usuarioSistema.getNome());
        alterarSenhaDTO.setUserName(usuarioSistema.getUserName());

        model.addAttribute("alterarSenhaDTO", alterarSenhaDTO);


        return "usuario/perfil";
    }


    @GetMapping("/cadastrar")
    public String cadastrar(ModelMap model) {
        model.addAttribute("novoUsuario", new Usuario());
        return "usuario/cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("novoUsuario") Usuario usuario, BindingResult result, RedirectAttributes attr) {

        List<Usuario> validateEmail = usuarioService.buscarTodos();

        for (Usuario user : validateEmail) {
            if(user.getEmail().equals(usuario.getEmail())){
                attr.addFlashAttribute("warning", "Este e-mail: "+ usuario.getEmail()+" já está em uso. Informe um novo e-mail.");

                return "redirect:/usuario/cadastrar";
            }
        }

        String senhaInicial = GeradorDeSenhaAleatoria.senhaInicial();
        usuario.setPassword(cripotografiaDeSenha.encodeSenha(senhaInicial));

        if (result.hasErrors()) {

            return "usuario/cadastro";
        }

        usuarioService.salvar(usuario);
        enviaEmailComSenhaInicial(usuario.getEmail(), usuario.getNome().toUpperCase(), usuario.getUserName(), senhaInicial);
        attr.addFlashAttribute("success", "Usuário inserido com sucesso. Um E-mail com a sua senha inicial foi enviado para: " + usuario.getEmail());

        return "redirect:/usuario/cadastrar";
    }

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        model.addAttribute("usuarios", usuarioService.buscarTodos());
        return "usuario/lista";

    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        Usuario novoUsuario = usuarioService.buscarPorId(id);
        model.addAttribute("novoUsuario", novoUsuario);
        return "usuario/cadastro";
    }

    @PostMapping("/editar")
    public String editar(@ModelAttribute("novoUsuario") Usuario usuario, BindingResult result, RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "usuario/cadastro";
        }

        Usuario usuarioExiste = usuarioService.buscarPorId(usuario.getId());

        if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            usuario.setPassword(usuarioExiste.getPassword());
        }

        usuarioService.editar(usuario);
        attr.addFlashAttribute("success", "Usuário editado com sucesso.");
        return "redirect:/usuario/cadastrar";
    }

    @PostMapping("/editar-perfil")
    public String editarUsuarioPerfil(@ModelAttribute("usuario") Usuario usuario, BindingResult result, RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "usuario/perfil";
        }

        Usuario usuarioExiste = usuarioService.buscarPorId(usuario.getId());

        if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            usuario.setPassword(usuarioExiste.getPassword());
        }

        usuarioService.editar(usuario);
        attr.addFlashAttribute("success", "Perfil - Usuário editado com sucesso.");
        return "redirect:/usuario/perfil";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, @ModelAttribute("userInfo") UserInfoDTO userInfoDTO, RedirectAttributes attr) {

        if (userInfoDTO.getUsuarioLogadoId().equals(id)) {
            attr.addFlashAttribute("error", "Não é permitido remover um Usuário logado.");
        } else {
            usuarioService.excluir(id);
            attr.addFlashAttribute("success", "Usuário removido com sucesso.");
        }
        return "redirect:/usuario/listar";
    }


    public void enviaEmailComSenhaInicial(String getEmail, String nome, String nomeUsuario, String senhaInicial) {
        Email email = new Email();
        email.setEmailFrom("access-manager@fleetcarservice.com.br");
        email.setEmailTo(getEmail);
        email.setSubject("BEM VINDO AO ALUGAMAIS SERVICE");
        email.setText("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "<style>\n" +
                "  .container {\n" +
                "    position: relative; \n" + /* Posicionamento relativo para o contêiner */
                "    width: 500px; \n" + /* Pode ser ajustado conforme necessário */
                "    height: 500px; \n" + /* Ou um valor fixo se você tiver um tamanho específico em mente */
                "    background: url('cid:backgroundImage') no-repeat center center;\n" +  /* Imagem centralizada */
                "    background-size: 100%; \n" + /* Imagem cobre todo o contêiner */
                "  }\n" +
                "  .content {\n" +
                "    position: absolute;\n" +  /* Posicionamento absoluto para o texto */
                "    top: 40%; \n" + /* Posiciona o texto a 40% abaixo do topo do contêiner */
                "    width: 100%; \n" + /* A .content ocupa a largura total */
                "    text-align: center; \n" + /* Centraliza o texto dentro da .content */
                "  }\n" +
                "  .text-style {\n" +
                "    color: gray; \n" + /* Estilização do texto */
                "    font-weight: bold; \n" + /* Texto em negrito */
                "    font-size: 20px; \n" +
                "  }\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"container\">\n" +
                "    <div class=\"content\">\n" +
                "    <br><br><br><br><br><br><br><br><br><br>" +
                "      <div class=\"text-style\">Olá, " + nome + "!</div>\n" +
                "      <div class=\"text-style\">Usuário: " + nomeUsuario + "</div>\n" +
                "      <div class=\"text-style\">Sua senha é: " + senhaInicial + "</div>\n" +
                "     <p>Caso queira alterar sua senha, no seu primeiro acesso procure seu nome no topo da tela e clique nele, depois selecione alterar senha e troque sua senha.</p>\n" +
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
            return "usuario/perfil";
        }


        Usuario usuarioExiste = usuarioService.buscarPorId(alterarSenhaDTO.getId());
        if (!passwordEncoder.matches(alterarSenhaDTO.getSenhaAtual(), usuarioExiste.getPassword())) {
            attr.addFlashAttribute("fail", "Senha atual não confere.");
            return "redirect:/usuario/perfil/";
        }
        if (!alterarSenhaDTO.getNovaSenha().equals(alterarSenhaDTO.getConfirmaSenha())) {
            attr.addFlashAttribute("fail", "A Nova senha não é igual a confirmação.");
            return "redirect:/usuario/perfil/";
        }

        usuarioService.updatePassword(alterarSenhaDTO.getId(), cripotografiaDeSenha.encodeSenha(alterarSenhaDTO.getNovaSenha()));
        attr.addFlashAttribute("success", "Senha de perfil - usuário alterado com sucesso.");
        return "redirect:/usuario/perfil";
    }

    @PostMapping("/recuperar-senha")
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

    @PostMapping("/upload-imagem")
    public String uploadImagem(@RequestParam("imagem") MultipartFile imagem, @RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.buscarPorId(id);

            if (usuario != null && !imagem.isEmpty()) {
                byte[] bytes = imagem.getBytes();
                usuario.setImagem(bytes); // Salvando a imagem no banco como BLOB

                usuarioService.editar(usuario);
                redirectAttributes.addFlashAttribute("mensagem", "Imagem atualizada com sucesso!");
            }
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao fazer upload da imagem.");
        }

        return "redirect:/usuario/perfil";
    }

    @PostMapping("/remover-imagem")
    public String removerImagem(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.buscarPorId(id);

        if (usuario != null) {
            usuario.setImagem(null); // Remove a imagem
            usuarioService.editar(usuario);
            redirectAttributes.addFlashAttribute("mensagem", "Imagem removida com sucesso!");
        }

        return "redirect:/usuario/perfil";
    }

    @GetMapping("/imagem/download/{id}")
    public ResponseEntity<byte[]> exibirImagem(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);

        if (usuario == null || usuario.getImagem() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Ou IMAGE_PNG conforme o tipo
                .body(usuario.getImagem());
    }


}
