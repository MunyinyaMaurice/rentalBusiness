package com.backend.rentalBusiness.auth.utils;

public class TenantContext {
    
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();
    
    public static void setTenantIdentifier(String tenantIdentifier) {
        currentTenant.set(tenantIdentifier);
    }
    
    public static String getTenantIdentifier() {
        return currentTenant.get();
    }
    
    public static void clear() {
        currentTenant.remove();
    }
}

