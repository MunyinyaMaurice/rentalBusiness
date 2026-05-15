package com.backend.rentalBusiness.payment.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePaymentRequest(

        UUID rentalId,
        BigDecimal amount,
        String method

) {}
