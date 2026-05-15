package com.backend.rentalBusiness.rentalTransaction.dto.response;

import java.util.UUID;

public record RemainingAssetResponse(

        UUID assetId,
        Integer rentedQuantity,
        Integer returnedQuantity,
        Integer remaining

) {}
