package com.backend.rentalBusiness.assetCategory.dto.response;

import java.util.UUID;

public record AssetCategoryResponse(

        UUID id,

        UUID businessId,

        String name,

        String description,

        String icon,

        Integer sortOrder,

        Boolean active

) {}