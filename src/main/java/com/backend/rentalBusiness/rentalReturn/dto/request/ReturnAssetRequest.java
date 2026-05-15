package com.backend.rentalBusiness.rentalReturn.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record ReturnAssetRequest(

        UUID rentalTransactionId,

        UUID assetId,

        Integer quantity,
        
        String conditionOnReturn,

        String damageType,

        String damageDescription,

        BigDecimal estimatedRepairCost

) {}