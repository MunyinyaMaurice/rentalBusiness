package com.backend.rentalBusiness.asset.service.impl;

import java.util.*;

import org.springframework.stereotype.Service;

import com.backend.rentalBusiness.asset.dto.request.CreateAssetRequest;
import com.backend.rentalBusiness.asset.dto.response.AssetResponse;
import com.backend.rentalBusiness.asset.entity.Asset;
import com.backend.rentalBusiness.asset.mapper.AssetMapper;
import com.backend.rentalBusiness.asset.repository.AssetRepository;
import com.backend.rentalBusiness.asset.service.AssetService;
import com.backend.rentalBusiness.assetCategory.repository.AssetCategoryRepository;
import com.backend.rentalBusiness.business.repository.BusinessRepository;
import com.backend.rentalBusiness.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final BusinessRepository businessRepository;
    private final StoreRepository storeRepository;
    private final AssetCategoryRepository categoryRepository;
    private final AssetMapper mapper;

    @Override
    public AssetResponse create(CreateAssetRequest request) {

        Asset asset = mapper.toEntity(request);

        asset.setBusiness(
                businessRepository.findById(request.businessId())
                        .orElseThrow(() -> new RuntimeException("Business not found"))
        );

        if (request.storeId() != null) {
            asset.setStore(
                    storeRepository.findById(request.storeId())
                            .orElseThrow(() -> new RuntimeException("Store not found"))
            );
        }

        if (request.categoryId() != null) {
            asset.setCategory(
                    categoryRepository.findById(request.categoryId())
                            .orElseThrow(() -> new RuntimeException("Category not found"))
            );
        }

        assetRepository.save(asset);

        return mapper.toResponse(asset);
    }

    @Override
    public AssetResponse get(UUID id) {

        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        return mapper.toResponse(asset);
    }

    @Override
    public List<AssetResponse> getByBusiness(UUID businessId) {

        return assetRepository.findByBusinessId(businessId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<AssetResponse> getByStore(UUID storeId) {

        return assetRepository.findByStoreId(storeId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public void delete(UUID id) {

        assetRepository.deleteById(id);
    }
}