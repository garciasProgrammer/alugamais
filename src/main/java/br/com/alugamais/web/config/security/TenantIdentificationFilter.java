package br.com.alugamais.web.config.security;

import br.com.alugamais.web.config.hibernate.TenantContext;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TenantIdentificationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extrai o subdomínio do domínio completo
        // Supõe que o subdomínio seja sempre a primeira parte do domínio
        String domain = request.getServerName();
        String[] parts = domain.split("\\.");
        String tenantId = parts.length > 1 ? parts[0] : null;

        try {
            TenantContext.setCurrentTenant(tenantId);
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }

    private String extractTenantIdFromRequest(HttpServletRequest request) {
        // Implemente a lógica de extração do identificador do tenant.
        // Este é apenas um exemplo.
        return request.getHeader("X-TenantID");
    }
}

