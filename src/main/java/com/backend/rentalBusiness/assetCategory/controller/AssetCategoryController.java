package com.backend.rentalBusiness.assetCategory.controller;

import java.util.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.rentalBusiness.assetCategory.dto.request.CreateAssetCategoryRequest;
import com.backend.rentalBusiness.assetCategory.dto.response.AssetCategoryResponse;
import com.backend.rentalBusiness.assetCategory.service.AssetCategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class AssetCategoryController {

    private final AssetCategoryService service;

    @PostMapping
    public AssetCategoryResponse create(@RequestBody CreateAssetCategoryRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public AssetCategoryResponse get(@PathVariable UUID id) {
        return service.get(id);
    }

    @GetMapping("/business/{businessId}")
    public List<AssetCategoryResponse> getByBusiness(@PathVariable UUID businessId) {
        return service.getByBusiness(businessId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}