package com.backend.rentalBusiness.auth.services;

import com.backend.rentalBusiness.auth.models.User;
import com.backend.rentalBusiness.auth.repositories.UserRepository;
import com.backend.rentalBusiness.auth.utils.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String tenantIdentifier = TenantContext.getTenantIdentifier();
        
        if (tenantIdentifier == null || tenantIdentifier.isEmpty()) {
            throw new UsernameNotFoundException("Tenant identifier not found in context");
        }

        User user = userRepository.findByEmailAndTenantIdentifierWithAuthorities(email, tenantIdentifier)
                .orElseThrow(() -> new UsernameNotFoundException(
                    "User not found with email: " + email + " for tenant: " + tenantIdentifier));

        if (!user.getTenant().getIsActive()) {
            throw new UsernameNotFoundException("Tenant is not active");
        }

        user.getAuthorities().size();
        return user;
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserByEmailAndTenant(String email, String tenantIdentifier) 
            throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndTenantIdentifierWithAuthorities(email, tenantIdentifier)
                .orElseThrow(() -> new UsernameNotFoundException(
                    "User not found with email: " + email + " for tenant: " + tenantIdentifier));

        if (!user.getTenant().getIsActive()) {
            throw new UsernameNotFoundException("Tenant is not active");
        }

        // Touch authorities while session is open (roles + permissions are EAGER but loaded lazily in batches)
        user.getAuthorities().size();

        return user;
    }
}

