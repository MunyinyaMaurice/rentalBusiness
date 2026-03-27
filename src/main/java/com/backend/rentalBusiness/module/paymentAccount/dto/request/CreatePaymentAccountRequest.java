package com.backend.rentalBusiness.module.paymentAccount.dto.request;

import java.util.UUID;

import jakarta.persistence.Column;

public record CreatePaymentAccountRequest(

        @Column(unique = true)
        UUID businessId,
        String provider,
        String country,
        String currency

) {}