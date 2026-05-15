package com.backend.rentalBusiness.store.service.impl;

import com.backend.rentalBusiness.business.entity.BusinessModel;
import com.backend.rentalBusiness.business.repository.BusinessRepository;
import com.backend.rentalBusiness.store.dto.request.CreateStoreRequest;
import com.backend.rentalBusiness.store.dto.request.UpdateStoreRequest;
import com.backend.rentalBusiness.store.dto.response.StoreResponse;
import com.backend.rentalBusiness.store.entity.Store;
import com.backend.rentalBusiness.store.mapper.StoreMapper;
import com.backend.rentalBusiness.store.repository.StoreRepository;
import com.backend.rentalBusiness.store.service.StoreService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final BusinessRepository businessRepository;
    private final StoreMapper mapper;

    @Override
    public StoreResponse create(CreateStoreRequest request) {

        BusinessModel business = businessRepository.findById(request.businessId())
        .orElseThrow(() -> new RuntimeException("Business not found"));

        Store store = mapper.toEntity(request);

        store.setBusiness(business);

        storeRepository.save(store);

        return mapper.toResponse(store);
    }

    @Override
    public StoreResponse get(UUID id) {

        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        return mapper.toResponse(store);
    }

    @Override
    public List<StoreResponse> getByBusiness(UUID businessId) {

        return storeRepository.findByBusinessId(businessId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
    @Override
public StoreResponse update(UUID id, UpdateStoreRequest request) {

    Store store = storeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Store not found"));

    store.setName(request.name());
    store.setLocation(request.location());
    store.setPhone(request.phone());
    store.setEmail(request.email());
    store.setAddress(request.address());
    store.setOperatingHours(request.operatingHours());
    store.setStatus(request.status());

    storeRepository.save(store);

    return mapper.toResponse(store);
}

    @Override
    public void delete(UUID id) {

        storeRepository.deleteById(id);
    }
}