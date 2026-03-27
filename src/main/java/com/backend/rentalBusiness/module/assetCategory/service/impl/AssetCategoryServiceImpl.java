package com.backend.rentalBusiness.module.assetCategory.service.impl;

import java.util.*;

import org.springframework.stereotype.Service;

import com.backend.rentalBusiness.module.assetCategory.dto.request.CreateAssetCategoryRequest;
import com.backend.rentalBusiness.module.assetCategory.dto.response.AssetCategoryResponse;
import com.backend.rentalBusiness.module.assetCategory.entity.AssetCategory;
import com.backend.rentalBusiness.module.assetCategory.mapper.AssetCategoryMapper;
import com.backend.rentalBusiness.module.assetCategory.repository.AssetCategoryRepository;
import com.backend.rentalBusiness.module.assetCategory.service.AssetCategoryService;
import com.backend.rentalBusiness.module.business.repository.BusinessRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssetCategoryServiceImpl implements AssetCategoryService {

    private final AssetCategoryRepository repository;
    private final BusinessRepository businessRepository;
    private final AssetCategoryMapper mapper;

    @Override
    public AssetCategoryResponse create(CreateAssetCategoryRequest request) {

        AssetCategory category = mapper.toEntity(request);

        category.setBusiness(
                businessRepository.findById(request.businessId())
                        .orElseThrow(() -> new RuntimeException("Business not found"))
        );

        repository.save(category);

        return mapper.toResponse(category);
    }

    @Override
    public AssetCategoryResponse get(UUID id) {

        AssetCategory category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return mapper.toResponse(category);
    }

    @Override
    public List<AssetCategoryResponse> getByBusiness(UUID businessId) {

        return repository.findByBusinessId(businessId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public void delete(UUID id) {

        repository.deleteById(id);
    }
}