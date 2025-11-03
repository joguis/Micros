package com.danielblanco.arquitecturasmodernas.microservices.user.config;

public class TenantContext {
    
    private static final ThreadLocal<Long> currentTenant = new ThreadLocal<>();
    
    public static void setCurrentTenant(Long zonaVeredalId) {
        currentTenant.set(zonaVeredalId);
    }
    
    public static Long getCurrentTenant() {
        return currentTenant.get();
    }
    
    public static void clear() {
        currentTenant.remove();
    }
}