package com.backend.rentalBusiness.auth.config;

import com.backend.rentalBusiness.auth.models.Permission;
import com.backend.rentalBusiness.auth.models.Role;
import com.backend.rentalBusiness.auth.models.Tenant;
import com.backend.rentalBusiness.auth.models.User;
import com.backend.rentalBusiness.auth.repositories.PermissionRepository;
import com.backend.rentalBusiness.auth.repositories.RoleRepository;
import com.backend.rentalBusiness.auth.repositories.TenantRepository;
import com.backend.rentalBusiness.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.super-admin.tenant-identifier:SYSTEM}")
    private String superAdminTenantIdentifier;

    @Value("${app.super-admin.business-name:System Business}")
    private String superAdminBusinessName;

    @Value("${app.super-admin.email:superadmin@rentalbusiness.local}")
    private String superAdminEmail;

    @Value("${app.super-admin.password:SuperAdmin@123}")
    private String superAdminPassword;

    @Value("${app.super-admin.first-name:Super}")
    private String superAdminFirstName;

    @Value("${app.super-admin.last-name:Admin}")
    private String superAdminLastName;

    @Override
    public void run(String... args) throws Exception {
        if (permissionRepository.count() == 0) {
            initializePermissions();
        }
        
        if (roleRepository.count() == 0) {
            initializeRoles();
        }

        ensureSystemSuperAdmin();
    }

    private void initializePermissions() {
        // User permissions
        createPermission("USER_CREATE", "Create user", "USER", "CREATE");
        createPermission("USER_READ", "Read user", "USER", "READ");
        createPermission("USER_UPDATE", "Update user", "USER", "UPDATE");
        createPermission("USER_DELETE", "Delete user", "USER", "DELETE");

        // Property permissions
        createPermission("PROPERTY_CREATE", "Create property", "PROPERTY", "CREATE");
        createPermission("PROPERTY_READ", "Read property", "PROPERTY", "READ");
        createPermission("PROPERTY_UPDATE", "Update property", "PROPERTY", "UPDATE");
        createPermission("PROPERTY_DELETE", "Delete property", "PROPERTY", "DELETE");

        // Booking permissions
        createPermission("BOOKING_CREATE", "Create booking", "BOOKING", "CREATE");
        createPermission("BOOKING_READ", "Read booking", "BOOKING", "READ");
        createPermission("BOOKING_UPDATE", "Update booking", "BOOKING", "UPDATE");
        createPermission("BOOKING_DELETE", "Delete booking", "BOOKING", "DELETE");

        // Payment permissions
        createPermission("PAYMENT_CREATE", "Create payment", "PAYMENT", "CREATE");
        createPermission("PAYMENT_READ", "Read payment", "PAYMENT", "READ");
        createPermission("PAYMENT_UPDATE", "Update payment", "PAYMENT", "UPDATE");
        createPermission("PAYMENT_DELETE", "Delete payment", "PAYMENT", "DELETE");

        // Review permissions
        createPermission("REVIEW_CREATE", "Create review", "REVIEW", "CREATE");
        createPermission("REVIEW_READ", "Read review", "REVIEW", "READ");
        createPermission("REVIEW_UPDATE", "Update review", "REVIEW", "UPDATE");
        createPermission("REVIEW_DELETE", "Delete review", "REVIEW", "DELETE");

        // Tenant permissions
        createPermission("TENANT_READ", "Read tenant", "TENANT", "READ");
        createPermission("TENANT_UPDATE", "Update tenant", "TENANT", "UPDATE");

        System.out.println("Permissions initialized successfully");
    }

    private void initializeRoles() {
        // SUPER_ADMIN - System-wide administrator
        Role superAdmin = createRole("SUPER_ADMIN", "System administrator with full access", false);
        Set<Permission> allPermissions = new HashSet<>(permissionRepository.findAll());
        superAdmin.setPermissions(allPermissions);
        roleRepository.save(superAdmin);

        // TENANT_ADMIN - Tenant administrator
        Role tenantAdmin = createRole("TENANT_ADMIN", "Tenant administrator", true);
        tenantAdmin.setPermissions(Set.of(
            getPermission("USER_CREATE"),
            getPermission("USER_READ"),
            getPermission("USER_UPDATE"),
            getPermission("USER_DELETE"),
            getPermission("PROPERTY_CREATE"),
            getPermission("PROPERTY_READ"),
            getPermission("PROPERTY_UPDATE"),
            getPermission("PROPERTY_DELETE"),
            getPermission("BOOKING_READ"),
            getPermission("BOOKING_UPDATE"),
            getPermission("PAYMENT_READ"),
            getPermission("REVIEW_READ"),
            getPermission("REVIEW_DELETE"),
            getPermission("TENANT_READ"),
            getPermission("TENANT_UPDATE")
        ));
        roleRepository.save(tenantAdmin);

        // MANAGER - Property manager
        Role manager = createRole("MANAGER", "Property manager", true);
        manager.setPermissions(Set.of(
            getPermission("PROPERTY_CREATE"),
            getPermission("PROPERTY_READ"),
            getPermission("PROPERTY_UPDATE"),
            getPermission("BOOKING_READ"),
            getPermission("BOOKING_UPDATE"),
            getPermission("PAYMENT_READ"),
            getPermission("REVIEW_READ")
        ));
        roleRepository.save(manager);

        // USER - Regular user
        Role user = createRole("USER", "Regular user", true);
        user.setPermissions(Set.of(
            getPermission("PROPERTY_READ"),
            getPermission("BOOKING_CREATE"),
            getPermission("BOOKING_READ"),
            getPermission("PAYMENT_CREATE"),
            getPermission("PAYMENT_READ"),
            getPermission("REVIEW_CREATE"),
            getPermission("REVIEW_READ"),
            getPermission("REVIEW_UPDATE")
        ));
        roleRepository.save(user);

        System.out.println("Roles initialized successfully");
    }

    private Permission createPermission(String name, String description, String resource, String action) {
        Permission permission = Permission.builder()
                .name(name)
                .description(description)
                .resource(resource)
                .action(action)
                .build();
        permissionRepository.save(Objects.requireNonNull(permission));
        return permission;
    }

    private Role createRole(String name, String description, boolean isTenantSpecific) {
        return Role.builder()
                .name(name)
                .description(description)
                .isTenantSpecific(isTenantSpecific)
                .permissions(new HashSet<>())
                .build();
    }

    private Permission getPermission(String name) {
        return permissionRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Permission not found: " + name));
    }

    private void ensureSystemSuperAdmin() {
        Role superAdminRole = roleRepository.findByName("SUPER_ADMIN")
                .orElseThrow(() -> new RuntimeException("SUPER_ADMIN role not found"));

        Tenant systemBusiness = tenantRepository.findByTenantIdentifier(superAdminTenantIdentifier)
                .orElse(null);
        if (systemBusiness == null) {
            Tenant newSystemBusiness = Tenant.builder()
                    .tenantIdentifier(superAdminTenantIdentifier)
                    .name(superAdminBusinessName)
                    .description("System business used for SUPER_ADMIN access")
                    .isActive(true)
                    .contactEmail(superAdminEmail)
                    .build();
            systemBusiness = tenantRepository.save(Objects.requireNonNull(newSystemBusiness));
        }

        User existingUser = userRepository.findByEmailAndTenantTenantIdentifier(
                superAdminEmail, superAdminTenantIdentifier).orElse(null);

        if (existingUser == null) {
            User superAdminUser = User.builder()
                    .email(superAdminEmail)
                    .password(passwordEncoder.encode(superAdminPassword))
                    .firstName(superAdminFirstName)
                    .lastName(superAdminLastName)
                    .tenant(systemBusiness)
                    .isActive(true)
                    .emailVerified(true)
                    .accountLocked(false)
                    .failedLoginAttempts(0)
                    .roles(Set.of(superAdminRole))
                    .build();
            userRepository.save(Objects.requireNonNull(superAdminUser));
            System.out.println("SUPER_ADMIN user created successfully");
        }
    }
}

