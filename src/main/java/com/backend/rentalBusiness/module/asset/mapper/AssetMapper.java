package com.backend.rentalBusiness.module.asset.mapper;

import com.backend.rentalBusiness.module.asset.dto.request.CreateAssetRequest;
import com.backend.rentalBusiness.module.asset.dto.response.AssetResponse;
import com.backend.rentalBusiness.module.asset.entity.Asset;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssetMapper {

    @Mapping(target = "business", ignore = true)
    @Mapping(target = "store", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "quantityAvailable", source = "quantityTotal")
    Asset toEntity(CreateAssetRequest request);

    @Mapping(target = "businessId", source = "business.id")
    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "categoryId", source = "category.id")
    AssetResponse toResponse(Asset asset);
}