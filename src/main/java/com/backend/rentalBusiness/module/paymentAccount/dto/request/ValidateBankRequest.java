package com.backend.rentalBusiness.module.paymentAccount.dto.request;

public record ValidateBankRequest(
        String accountNumber,
        String bankCode
) {}
