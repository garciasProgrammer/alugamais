package br.com.alugamais.web.config.hibernate;

import br.com.alugamais.web.controller.SessionController;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

            String tenantId = extractTenantIdFromRequest(request);
            TenantContext.setCurrentTenant(tenantId);
            SessionController.setTenantIdSession(request, tenantId);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        TenantContext.clear();
    }

    private String extractTenantIdFromRequest(HttpServletRequest request) {

        String host = request.getServerName();
        return host.split("\\.")[0];
    }

}
