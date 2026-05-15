package com.backend.rentalBusiness.paymentAccount.dto.response;

public record OnboardBusinessResponse(
        String message,
        String accountName,
        String subaccountId,
        boolean alreadyOnboarded,
        boolean success
) {}