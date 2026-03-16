package com.backend.rentalBusiness.module.store.dto.response;

import java.util.UUID;

public record StoreResponse(

        UUID id,
        UUID businessId,
        String name,
        String location,
        String phone,
        String email,
        String address,
        String operatingHours,
        String status
) {}