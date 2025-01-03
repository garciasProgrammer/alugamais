package br.com.alugamais.web.controller;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class SessionController {

    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Não criar uma nova se não existir
        if (session != null) {
            session.invalidate();
        }
    }

    public static void renovarSessao(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        request.getSession(true); // Cria uma nova sessão
    }

    public static void setTenantIdSession(HttpServletRequest request, String tenantId){
        HttpSession session = request.getSession(true); // true para criar a sessão se ela não existir
        session.setAttribute("CURRENT_TENANT", tenantId);
    }
}
