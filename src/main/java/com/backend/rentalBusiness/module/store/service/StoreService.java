package com.backend.rentalBusiness.module.store.service;

import com.backend.rentalBusiness.module.store.dto.request.CreateStoreRequest;
import com.backend.rentalBusiness.module.store.dto.request.UpdateStoreRequest;
import com.backend.rentalBusiness.module.store.dto.response.StoreResponse;

import java.util.List;
import java.util.UUID;

public interface StoreService {

    StoreResponse create(CreateStoreRequest request);

    StoreResponse get(UUID id);

    List<StoreResponse> getByBusiness(UUID businessId);
    
    StoreResponse update(UUID id, UpdateStoreRequest request);

    void delete(UUID id);
}