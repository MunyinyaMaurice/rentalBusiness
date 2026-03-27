package com.backend.rentalBusiness.module.rentalTransaction.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateRentalLineRequest(

        UUID assetId,
        Integer quantity,
        BigDecimal price

) {}
