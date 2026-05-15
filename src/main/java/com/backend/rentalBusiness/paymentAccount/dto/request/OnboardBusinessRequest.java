package com.backend.rentalBusiness.paymentAccount.dto.request;

import java.util.UUID;

import jakarta.persistence.Column;

public record OnboardBusinessRequest(

        @Column(unique = true)
        UUID businessId,
        String bankCode,
        String accountNumber,
        Double splitRatio

) {}