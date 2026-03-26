package com.backend.rentalBusiness.auth.repositories;

import com.backend.rentalBusiness.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmailAndTenantTenantIdentifier(String email, String tenantIdentifier);

    /**
     * Loads user + tenant + roles (and nested permissions via EAGER on Role) in one query.
     * Used by JWT filter so authorities are available outside a request transaction.
     */
    @Query("""
            SELECT DISTINCT u FROM User u
            JOIN FETCH u.tenant t
            LEFT JOIN FETCH u.roles r
            WHERE u.email = :email AND t.tenantIdentifier = :tenantIdentifier
            """)
    Optional<User> findByEmailAndTenantIdentifierWithAuthorities(
            @Param("email") String email,
            @Param("tenantIdentifier") String tenantIdentifier);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmailAndTenantId(String email, Long tenantId);
    
    List<User> findByTenantId(Long tenantId);
    
    List<User> findByTenantTenantIdentifier(String tenantIdentifier);
    
    @Query("SELECT u FROM User u WHERE u.tenant.tenantIdentifier = :tenantIdentifier AND u.isActive = true")
    List<User> findActiveUsersByTenant(String tenantIdentifier);
}

