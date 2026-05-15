package com.backend.rentalBusiness.paymentAccount.dto.response;

public record ValidateBankResponse(
        String accountName,
        boolean valid
) {}