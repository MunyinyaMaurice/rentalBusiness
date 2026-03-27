package com.backend.rentalBusiness.module.rentalReturn.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ReturnAssetResponse(

        UUID id,
        UUID rentalTransactionId,
        UUID assetId,
        Instant returnDate,
        Boolean lateReturn,
        Integer daysLate,
        BigDecimal lateFee,
        BigDecimal damageCharge

) {}