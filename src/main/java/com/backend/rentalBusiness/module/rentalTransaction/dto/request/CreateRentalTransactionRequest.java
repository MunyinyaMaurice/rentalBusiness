package com.backend.rentalBusiness.module.rentalTransaction.dto.request;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;

public record CreateRentalTransactionRequest(
        
        @Column(unique = true)
        UUID businessId,
        UUID storeId,

        Instant rentalDate,
        Instant dueDate,

        Integer rentalDuration,

        List<CreateRentalLineRequest> lines

) {}