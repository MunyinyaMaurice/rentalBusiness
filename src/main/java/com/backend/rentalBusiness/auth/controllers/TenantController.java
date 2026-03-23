package com.backend.rentalBusiness.auth.controllers;

import com.backend.rentalBusiness.auth.dto.TenantResponse;
import com.backend.rentalBusiness.auth.services.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/api/businesses", "/api/tenants"})
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'TENANT_ADMIN')")
    public ResponseEntity<List<TenantResponse>> getTenants(Authentication authentication) {
        return ResponseEntity.ok(tenantService.getAccessibleTenants(authentication));
    }

    @GetMapping("/{businessIdentifier}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'TENANT_ADMIN')")
    public ResponseEntity<TenantResponse> getTenantByIdentifier(
            @PathVariable String businessIdentifier,
            Authentication authentication) {
        return ResponseEntity.ok(tenantService.getTenantByIdentifier(businessIdentifier, authentication));
    }

    @PatchMapping("/{businessIdentifier}/activate")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<TenantResponse> activateBusiness(@PathVariable String businessIdentifier) {
        return ResponseEntity.ok(tenantService.activateBusiness(businessIdentifier));
    }

    @PatchMapping("/{businessIdentifier}/deactivate")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<TenantResponse> deactivateBusiness(@PathVariable String businessIdentifier) {
        return ResponseEntity.ok(tenantService.deactivateBusiness(businessIdentifier));
    }
}
