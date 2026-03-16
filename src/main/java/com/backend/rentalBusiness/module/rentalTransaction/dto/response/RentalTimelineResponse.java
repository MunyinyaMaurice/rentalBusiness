package com.backend.rentalBusiness.module.rentalTransaction.dto.response;

import java.time.Instant;

public record RentalTimelineResponse(

        Instant timestamp,
        String eventType,
        String description

) {}
