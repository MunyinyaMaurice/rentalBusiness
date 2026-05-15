package com.backend.rentalBusiness.rentalTransaction.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

// public record RentalLineResponse(

//         UUID assetId,

//         Integer quantity,

//         BigDecimal price,

//         BigDecimal lineTotal

// ) {}
public record RentalLineResponse(

        UUID assetId,
        Integer quantity,
        Integer returnedQuantity,
        Integer remaining,
        BigDecimal price,
        BigDecimal lineTotal

) {}