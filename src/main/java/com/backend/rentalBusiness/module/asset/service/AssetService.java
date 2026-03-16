package com.backend.rentalBusiness.module.asset.service;

import com.backend.rentalBusiness.module.asset.dto.request.CreateAssetRequest;
import com.backend.rentalBusiness.module.asset.dto.response.AssetResponse;

import java.util.List;
import java.util.UUID;

public interface AssetService {

    AssetResponse create(CreateAssetRequest request);

    AssetResponse get(UUID id);

    List<AssetResponse> getByBusiness(UUID businessId);

    List<AssetResponse> getByStore(UUID storeId);

    void delete(UUID id);
}