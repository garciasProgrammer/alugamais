package br.com.alugamais.web.controller;


import br.com.alugamais.web.config.hibernate.TenantContext;
import br.com.alugamais.web.domain.CardUpdate;
import br.com.alugamais.web.domain.CardUpdateItensLista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate template;

    @Autowired
    public WebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    // Método para enviar atualizações
    public void sendCardUpdate(CardUpdate updateInfo) {
        String tenantId = TenantContext.getCurrentTenant();
        template.convertAndSend(String.format("/topic/%s/cards", tenantId), updateInfo);
    }

    public void sendCardUpdate(CardUpdateItensLista cardUpdateItensLista) {
        String tenantId = TenantContext.getCurrentTenant();
        template.convertAndSend(String.format("/topic/%s/cards", tenantId), cardUpdateItensLista);
    }
}
