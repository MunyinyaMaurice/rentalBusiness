package com.backend.rentalBusiness.assetCategory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateAssetCategoryRequest(

        @NotNull
        UUID businessId,

        @NotBlank
        String name,

        String description,

        String icon,

        Integer sortOrder

) {}