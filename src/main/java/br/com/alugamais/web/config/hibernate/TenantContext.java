package br.com.alugamais.web.config.hibernate;

import java.util.Arrays;
import java.util.List;

public class TenantContext {

    private static ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static void setCurrentTenant(String tenantId) {
        currentTenant.set(tenantId);
    }

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }

    public static List getListTenants() {
        List<String> tenants = Arrays.
                asList("residencial-sofia");
        return tenants;
    }
}

