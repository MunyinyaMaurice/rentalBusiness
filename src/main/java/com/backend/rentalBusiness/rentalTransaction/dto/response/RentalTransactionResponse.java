package com.backend.rentalBusiness.rentalTransaction.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RentalTransactionResponse(

        UUID id,

        UUID businessId,

        UUID storeId,

        Instant rentalDate,

        Instant dueDate,

        Integer rentalDuration,

        BigDecimal subtotal,

        BigDecimal totalAmount,

        String status,

        List<RentalLineResponse> lines

) {}