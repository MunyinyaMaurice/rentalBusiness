package com.backend.rentalBusiness.module.asset.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateAssetRequest(

        @NotNull
        UUID businessId,

        UUID storeId,

        UUID categoryId,

        String name,

        String serialNumber,

        String description,

        BigDecimal rentalPrice,

        Integer quantityTotal,

        Integer minRentalPeriod,

        Integer maxRentalPeriod,

        String specifications,

        String metadata

) {}