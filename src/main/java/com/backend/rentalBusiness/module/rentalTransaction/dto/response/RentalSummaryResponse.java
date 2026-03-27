package com.backend.rentalBusiness.module.rentalTransaction.dto.response;

import java.util.UUID;

import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalStatus;

public record RentalSummaryResponse(

        UUID rentalId,
        int totalAssets,
        int returnedAssets,
        int remainingAssets,
        boolean fullyReturned,
        boolean overdue,
        RentalStatus status

) {}