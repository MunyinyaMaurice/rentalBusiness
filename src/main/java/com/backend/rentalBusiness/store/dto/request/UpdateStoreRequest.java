package com.backend.rentalBusiness.store.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateStoreRequest(

        @NotBlank
        String name,

        String location,

        String phone,

        String email,

        String address,

        String operatingHours,

        String status
) {}