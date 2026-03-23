package com.backend.rentalBusiness.auth.services;

import com.backend.rentalBusiness.auth.dto.*;
import com.backend.rentalBusiness.auth.exceptions.*;
import com.backend.rentalBusiness.auth.models.Role;
import com.backend.rentalBusiness.auth.models.Tenant;
import com.backend.rentalBusiness.auth.models.User;
import com.backend.rentalBusiness.auth.repositories.RoleRepository;
import com.backend.rentalBusiness.auth.repositories.TenantRepository;
import com.backend.rentalBusiness.auth.repositories.UserRepository;
import com.backend.rentalBusiness.auth.utils.JwtTokenProvider;
import com.backend.rentalBusiness.auth.utils.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        // Verify tenant exists and is active
        Tenant tenant = tenantRepository.findByTenantIdentifierAndIsActive(
                loginRequest.getTenantIdentifier(), true)
                .orElseThrow(() -> new TenantNotFoundException(
                    "Tenant not found or inactive: " + loginRequest.getTenantIdentifier()));

        // Find user
        User user = userRepository.findByEmailAndTenantTenantIdentifier(
                loginRequest.getEmail(), loginRequest.getTenantIdentifier())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        // Check if account is locked
        if (user.getAccountLocked()) {
            throw new InvalidCredentialsException("Account is locked. Please contact administrator.");
        }

        // Check if account is active
        if (!user.getIsActive()) {
            throw new InvalidCredentialsException("Account is not active.");
        }

        // Authenticate (DaoAuthenticationProvider uses CustomUserDetailsService which reads TenantContext)
        try {
            TenantContext.setTenantIdentifier(loginRequest.getTenantIdentifier());
            try {
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                    )
                );
            } finally {
                TenantContext.clear();
            }

            // Update last login and reset failed attempts
            user.setLastLoginAt(LocalDateTime.now());
            user.setFailedLoginAttempts(0);
            userRepository.save(user);

            // Generate token
            String token = jwtTokenProvider.generateToken(user);
            Date expirationDate = jwtTokenProvider.getExpirationDateFromToken(token);

            // Build response
            return AuthResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .userId(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .tenantIdentifier(tenant.getTenantIdentifier())
                    .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                    .permissions(user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()))
                    .expiresAt(expirationDate.toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDateTime())
                    .build();

        } catch (BadCredentialsException e) {
            // Increment failed login attempts
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            
            // Lock account after 5 failed attempts
            if (user.getFailedLoginAttempts() >= 5) {
                user.setAccountLocked(true);
            }
            
            userRepository.save(user);
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    @Transactional
    public UserResponse register(RegisterRequest registerRequest) {
        // Verify tenant exists and is active
        Tenant tenant = tenantRepository.findByTenantIdentifierAndIsActive(
                registerRequest.getTenantIdentifier(), true)
                .orElseThrow(() -> new TenantNotFoundException(
                    "Tenant not found or inactive: " + registerRequest.getTenantIdentifier()));

        // Check if user already exists
        if (userRepository.existsByEmailAndTenantId(registerRequest.getEmail(), tenant.getId())) {
            throw new UserAlreadyExistsException(
                "User already exists with email: " + registerRequest.getEmail());
        }

        // Get default USER role
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default USER role not found"));

        // Create user
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .phoneNumber(registerRequest.getPhoneNumber())
                .tenant(tenant)
                .isActive(true)
                .emailVerified(false)
                .accountLocked(false)
                .failedLoginAttempts(0)
                .roles(new HashSet<>(Set.of(userRole)))
                .build();

        user = userRepository.save(user);

        // Build response
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .tenantIdentifier(tenant.getTenantIdentifier())
                .isActive(user.getIsActive())
                .emailVerified(user.getEmailVerified())
                .roles(user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet()))
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Transactional
    public AuthResponse registerTenant(TenantRegistrationRequest request) {
        // Check if tenant already exists
        if (tenantRepository.existsByTenantIdentifier(request.getTenantIdentifier())) {
            throw new TenantAlreadyExistsException(
                "Tenant already exists with identifier: " + request.getTenantIdentifier());
        }

        // Create tenant
        Tenant tenant = Tenant.builder()
                .tenantIdentifier(request.getTenantIdentifier())
                .name(request.getName())
                .description(request.getDescription())
                .contactEmail(request.getContactEmail())
                .contactPhone(request.getContactPhone())
                .address(request.getAddress())
                .isActive(true)
                .build();

        tenant = tenantRepository.save(tenant);

        // Get TENANT_ADMIN role
        Role adminRole = roleRepository.findByName("TENANT_ADMIN")
                .orElseThrow(() -> new RuntimeException("TENANT_ADMIN role not found"));

        // Create admin user
        User adminUser = User.builder()
                .email(request.getAdminEmail())
                .password(passwordEncoder.encode(request.getAdminPassword()))
                .firstName(request.getAdminFirstName())
                .lastName(request.getAdminLastName())
                .phoneNumber(request.getAdminPhone())
                .tenant(tenant)
                .isActive(true)
                .emailVerified(true) // Auto-verify admin
                .accountLocked(false)
                .failedLoginAttempts(0)
                .roles(new HashSet<>(Set.of(adminRole)))
                .build();

        adminUser = userRepository.save(adminUser);

        // Generate token
        String token = jwtTokenProvider.generateToken(adminUser);
        Date expirationDate = jwtTokenProvider.getExpirationDateFromToken(token);

        // Build response
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(adminUser.getId())
                .email(adminUser.getEmail())
                .firstName(adminUser.getFirstName())
                .lastName(adminUser.getLastName())
                .tenantIdentifier(tenant.getTenantIdentifier())
                .roles(adminUser.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet()))
                .permissions(adminUser.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet()))
                .expiresAt(expirationDate.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDateTime())
                .build();
    }
}

