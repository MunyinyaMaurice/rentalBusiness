package com.backend.rentalBusiness.assetCategory.mapper;

import com.backend.rentalBusiness.assetCategory.dto.request.CreateAssetCategoryRequest;
import com.backend.rentalBusiness.assetCategory.dto.response.AssetCategoryResponse;
import com.backend.rentalBusiness.assetCategory.entity.AssetCategory;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssetCategoryMapper {

    @Mapping(target = "business", ignore = true)
    AssetCategory toEntity(CreateAssetCategoryRequest request);

    @Mapping(target = "businessId", source = "business.id")
    AssetCategoryResponse toResponse(AssetCategory category);
}