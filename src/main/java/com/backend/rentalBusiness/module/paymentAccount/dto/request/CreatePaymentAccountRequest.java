package com.backend.rentalBusiness.module.paymentAccount.dto.request;

import java.util.UUID;

public record CreatePaymentAccountRequest(

        UUID businessId,
        String provider,
        String country,
        String currency

) {}