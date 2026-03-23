package com.backend.rentalBusiness.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantResponse {
    private Long id;
    private String tenantIdentifier;
    private String name;
    private String description;
    private Boolean isActive;
    private String contactEmail;
    private String contactPhone;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
