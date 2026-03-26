package com.backend.rentalBusiness.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantRegistrationRequest {

    @NotBlank(message = "Tenant identifier is required")
    @Size(min = 3, max = 100, message = "Tenant identifier must be between 3 and 100 characters")
    private String tenantIdentifier;

    @NotBlank(message = "Tenant name is required")
    @Size(min = 2, max = 255, message = "Tenant name must be between 2 and 255 characters")
    private String name;

    private String description;
    private String contactEmail;
    private String contactPhone;
    private String address;

    // Admin user details
    @NotBlank(message = "Admin email is required")
    private String adminEmail;

    @NotBlank(message = "Admin password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String adminPassword;

    @NotBlank(message = "Admin first name is required")
    private String adminFirstName;

    @NotBlank(message = "Admin last name is required")
    private String adminLastName;

    private String adminPhone;
}

