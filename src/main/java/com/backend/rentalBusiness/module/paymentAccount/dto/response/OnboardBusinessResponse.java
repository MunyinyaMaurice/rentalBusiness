package com.backend.rentalBusiness.module.paymentAccount.dto.response;

public record OnboardBusinessResponse(
        String message,
        String accountName,
        String subaccountId,
        boolean alreadyOnboarded,
        boolean success
) {}