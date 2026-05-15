package com.backend.rentalBusiness.paymentAccount.dto.request;

import java.util.UUID;

public record CreateSubaccountRequest(

        UUID businessId,
        String bankCode,
        String accountNumber,
        String businessName,
        String businessEmail,
        Double splitRatio

) {}