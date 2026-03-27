package com.backend.rentalBusiness.module.paymentAccount.dto.response;

public record ValidateBankResponse(
        String accountName,
        boolean valid
) {}