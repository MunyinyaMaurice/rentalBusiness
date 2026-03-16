package com.backend.rentalBusiness.module.assetCategory.service;

import com.backend.rentalBusiness.module.assetCategory.dto.request.CreateAssetCategoryRequest;
import com.backend.rentalBusiness.module.assetCategory.dto.response.AssetCategoryResponse;

import java.util.List;
import java.util.UUID;

public interface AssetCategoryService {

    AssetCategoryResponse create(CreateAssetCategoryRequest request);

    AssetCategoryResponse get(UUID id);

    List<AssetCategoryResponse> getByBusiness(UUID businessId);

    void delete(UUID id);
}