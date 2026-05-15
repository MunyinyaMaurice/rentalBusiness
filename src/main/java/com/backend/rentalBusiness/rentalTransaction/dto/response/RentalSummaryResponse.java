package com.backend.rentalBusiness.rentalTransaction.dto.response;

import java.util.UUID;

import com.backend.rentalBusiness.rentalTransaction.entity.RentalStatus;

public record RentalSummaryResponse(

        UUID rentalId,
        int totalAssets,
        int returnedAssets,
        int remainingAssets,
        boolean fullyReturned,
        boolean overdue,
        RentalStatus status

) {}