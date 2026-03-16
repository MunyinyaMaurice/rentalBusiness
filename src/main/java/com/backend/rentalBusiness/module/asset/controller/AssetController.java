package com.backend.rentalBusiness.module.asset.controller;

import java.util.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.rentalBusiness.module.asset.dto.request.CreateAssetRequest;
import com.backend.rentalBusiness.module.asset.dto.response.AssetResponse;
import com.backend.rentalBusiness.module.asset.service.AssetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @PostMapping
    public AssetResponse create(@RequestBody CreateAssetRequest request) {
        return assetService.create(request);
    }

    @GetMapping("/{id}")
    public AssetResponse get(@PathVariable UUID id) {
        return assetService.get(id);
    }

    @GetMapping("/business/{businessId}")
    public List<AssetResponse> getByBusiness(@PathVariable UUID businessId) {
        return assetService.getByBusiness(businessId);
    }

    @GetMapping("/store/{storeId}")
    public List<AssetResponse> getByStore(@PathVariable UUID storeId) {
        return assetService.getByStore(storeId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        assetService.delete(id);
    }
}