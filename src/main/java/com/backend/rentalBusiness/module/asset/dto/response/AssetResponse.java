package com.backend.rentalBusiness.module.asset.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record AssetResponse(

        UUID id,

        UUID businessId,

        UUID storeId,

        UUID categoryId,

        String name,

        String serialNumber,

        String description,

        BigDecimal rentalPrice,

        Integer quantityTotal,

        Integer quantityAvailable,

        String condition,

        String status

) {}