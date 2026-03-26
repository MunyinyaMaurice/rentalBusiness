package com.backend.rentalBusiness.auth.services;

import com.backend.rentalBusiness.auth.dto.TenantResponse;
import com.backend.rentalBusiness.auth.exceptions.TenantNotFoundException;
import com.backend.rentalBusiness.auth.models.Tenant;
import com.backend.rentalBusiness.auth.models.User;
import com.backend.rentalBusiness.auth.repositories.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;

    public List<TenantResponse> getAccessibleTenants(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        boolean isSuperAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_SUPER_ADMIN".equals(authority.getAuthority()));

        if (isSuperAdmin) {
            return tenantRepository.findAll().stream()
                    .map(this::toTenantResponse)
                    .collect(Collectors.toList());
        }

        return List.of(toTenantResponse(currentUser.getTenant()));
    }

    public TenantResponse getTenantByIdentifier(String tenantIdentifier, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        boolean isSuperAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_SUPER_ADMIN".equals(authority.getAuthority()));

        if (!isSuperAdmin && !currentUser.getTenant().getTenantIdentifier().equals(tenantIdentifier)) {
            throw new TenantNotFoundException("Tenant not found: " + tenantIdentifier);
        }

        Tenant tenant = tenantRepository.findByTenantIdentifier(tenantIdentifier)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found: " + tenantIdentifier));

        return toTenantResponse(tenant);
    }

    public TenantResponse activateBusiness(String businessIdentifier) {
        return updateBusinessStatus(businessIdentifier, true);
    }

    public TenantResponse deactivateBusiness(String businessIdentifier) {
        return updateBusinessStatus(businessIdentifier, false);
    }

    private TenantResponse updateBusinessStatus(String businessIdentifier, boolean isActive) {
        Tenant tenant = tenantRepository.findByTenantIdentifier(businessIdentifier)
                .orElseThrow(() -> new TenantNotFoundException("Business not found: " + businessIdentifier));
        tenant.setIsActive(isActive);
        tenant = tenantRepository.save(tenant);
        return toTenantResponse(tenant);
    }

    private TenantResponse toTenantResponse(Tenant tenant) {
        return TenantResponse.builder()
                .id(tenant.getId())
                .tenantIdentifier(tenant.getTenantIdentifier())
                .name(tenant.getName())
                .description(tenant.getDescription())
                .isActive(tenant.getIsActive())
                .contactEmail(tenant.getContactEmail())
                .contactPhone(tenant.getContactPhone())
                .address(tenant.getAddress())
                .createdAt(tenant.getCreatedAt())
                .updatedAt(tenant.getUpdatedAt())
                .build();
    }
}
