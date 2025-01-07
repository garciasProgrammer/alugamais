package br.com.alugamais.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        String logoPath = "/image/icon-login-blackBlue.png"; // Logo padrão
        String domain = request.getServerName(); // Obtém o domínio completo

        // Extrai o subdomínio
        String subdomain = domain.split("\\.")[0];

        // Define o caminho da logo baseado no subdomínio
        if (subdomain != null && !subdomain.isEmpty()) {
            String logoFilePath = "classpath:/static/image/" + subdomain + "_logo.png";

            Resource logoResource = resourceLoader.getResource(logoFilePath);

            try {
                // Verifica se o recurso existe, sem acessar o sistema de arquivos diretamente
                if (logoResource.exists()) {
                    logoPath = "/image/" + subdomain + "_logo.png"; // Caminho se a logo existir
                }
            } catch (Exception e) {
                // Se ocorrer um erro ao acessar o recurso, mantém a logo padrão
                e.printStackTrace();
            }
        }

        // Adiciona o caminho da logo ao modelo
        model.addAttribute("logoPath", logoPath);

        return "login"; // Renderiza a página de login com a logo correta
    }

    @PostMapping("/logar")
    public String logar() {

        return "redirect:/home";
    }

    @GetMapping("/recuperar-senha")
    public String recuperarSenha(){
        return "recuperar-senha";
    }
}
