package com.backend.rentalBusiness.business.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record BusinessResponse(

        UUID id,
        String businessName,
        String businessType,
        String contactPerson,
        String phone,
        String email,
        String address,
        String timezone,
        String currency,
        BigDecimal taxRate,
        Boolean onboardingCompleted,
        Instant createdAt,
        Boolean active

) {}