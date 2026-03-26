package com.backend.rentalBusiness.auth.repositories;

import com.backend.rentalBusiness.auth.models.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    
    Optional<Tenant> findByTenantIdentifier(String tenantIdentifier);
    
    boolean existsByTenantIdentifier(String tenantIdentifier);
    
    Optional<Tenant> findByTenantIdentifierAndIsActive(String tenantIdentifier, Boolean isActive);
}

