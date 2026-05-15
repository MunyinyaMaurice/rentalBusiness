package com.backend.rentalBusiness.payment.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentResponse(

        UUID id,
        UUID rentalId,
        BigDecimal amount,
        String method,
        String status,
        String paymentUrl

) {}