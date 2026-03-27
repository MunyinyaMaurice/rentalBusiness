package com.backend.rentalBusiness.module.paymentAccount.dto.response;

import java.util.UUID;

public record PaymentAccountResponse(

        UUID id,
        UUID businessId,
        String provider,
        String providerAccountId,
        Boolean payoutsEnabled,
        Boolean detailsSubmitted

) {}