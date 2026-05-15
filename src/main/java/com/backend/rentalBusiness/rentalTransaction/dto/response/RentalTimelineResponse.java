package com.backend.rentalBusiness.rentalTransaction.dto.response;

import java.time.Instant;

public record RentalTimelineResponse(

        Instant timestamp,
        String eventType,
        String description

) {}
