package br.com.alugamais.web.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
    private String tenantId;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String tenantId) {
        super(username, password, authorities);
        this.tenantId = tenantId;
    }

    // Getters and setters
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
