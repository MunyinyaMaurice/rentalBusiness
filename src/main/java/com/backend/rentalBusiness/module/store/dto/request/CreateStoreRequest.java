package com.backend.rentalBusiness.module.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateStoreRequest(

        @NotNull
        UUID businessId,

        @NotBlank
        String name,

        String location,
        String phone,
        String email,
        String address,
        String operatingHours,
        String status
) {}