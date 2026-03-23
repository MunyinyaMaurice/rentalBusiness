package com.backend.rentalBusiness.auth.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;
    @Builder.Default
    private String type = "Bearer";
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String tenantIdentifier;
    private Set<String> roles;
    private Set<String> permissions;
    private LocalDateTime expiresAt;
}

