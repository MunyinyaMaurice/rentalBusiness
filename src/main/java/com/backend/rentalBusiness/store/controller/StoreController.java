package com.backend.rentalBusiness.store.controller;

import com.backend.rentalBusiness.store.dto.request.CreateStoreRequest;
import com.backend.rentalBusiness.store.dto.request.UpdateStoreRequest;
import com.backend.rentalBusiness.store.dto.response.StoreResponse;
import com.backend.rentalBusiness.store.service.StoreService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public StoreResponse create(@RequestBody CreateStoreRequest request) {
        return storeService.create(request);
    }

    @GetMapping("/{id}")
    public StoreResponse get(@PathVariable UUID id) {
        return storeService.get(id);
    }

    @GetMapping("/business/{businessId}")
    public List<StoreResponse> getByBusiness(@PathVariable UUID businessId) {
        return storeService.getByBusiness(businessId);
    }
    @PutMapping("/{id}")
    public StoreResponse update(
            @PathVariable UUID id,
            @RequestBody UpdateStoreRequest request
    ) {
        return storeService.update(id, request);
}

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        storeService.delete(id);
    }
}