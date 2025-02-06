package br.com.alugamais.web.controller;

import br.com.alugamais.service.UsuarioService;
import br.com.alugamais.web.config.security.CustomUserDetails;
import br.com.alugamais.web.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserAttributes {

    @Autowired
    private UsuarioService usuarioService;

    @ModelAttribute
    public void addUserAttributes(Model model) {
        // Obtém a autenticação do Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se há um usuário autenticado
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            // Se for um UserDetails, então é um usuário autenticado
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                Usuario usuario = usuarioService.buscarPorUserName(CustomUserDetails.getUsuarioLogado());
                String usuarioLogado = CustomUserDetails.getNomeUsuarioLogado().toUpperCase();

                // Adicionando os dados ao modelo, disponíveis em TODAS as páginas
                model.addAttribute("usuario", usuario);
                model.addAttribute("usuarioNome", usuario.getNome().toUpperCase());
                model.addAttribute("usuarioLogado", usuarioLogado);
                model.addAttribute("tipoUsuario", usuario.getTipoDeUsuario());
            }
        }
    }
}

