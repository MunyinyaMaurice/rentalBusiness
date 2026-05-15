package com.backend.rentalBusiness.paymentAccount.dto.request;

public record ValidateBankRequest(
        String accountNumber,
        String bankCode
) {}
