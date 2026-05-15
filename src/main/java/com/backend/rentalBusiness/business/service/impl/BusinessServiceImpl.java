package com.backend.rentalBusiness.business.service.impl;

import com.backend.rentalBusiness.common.exception.ApiException;
import com.backend.rentalBusiness.common.exception.ResourceNotFoundException;
import com.backend.rentalBusiness.business.dto.request.CreateBusinessRequest;
import com.backend.rentalBusiness.business.dto.request.UpdateBusinessRequest;
import com.backend.rentalBusiness.business.dto.response.BusinessResponse;
import com.backend.rentalBusiness.business.entity.BusinessModel;
import com.backend.rentalBusiness.business.mapper.BusinessMapper;
import com.backend.rentalBusiness.business.repository.BusinessRepository;
import com.backend.rentalBusiness.business.service.BusinessService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository repository;
    private final BusinessMapper mapper;

    @Override
    public BusinessResponse create(CreateBusinessRequest request) {

        BusinessModel entity = mapper.toEntity(request);
        
        if(repository.findByEmail(request.email()).isPresent()){
    throw new ApiException("Business with this email already exists");
    }

        repository.save(entity);

        return mapper.toResponse(entity);
    }

    @Override
    public BusinessResponse get(UUID id) {

        BusinessModel entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business"));

        return mapper.toResponse(entity);
    }

    @Override
    public Page<BusinessResponse> getAll(Pageable pageable) {

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    public BusinessResponse update(UUID id, UpdateBusinessRequest request) {

        BusinessModel entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business"));

        if (request.businessName() != null)
            entity.setBusinessName(request.businessName());

        if (request.businessType() != null)
            entity.setBusinessType(request.businessType());

        if (request.contactPerson() != null)
            entity.setContactPerson(request.contactPerson());

        if (request.phone() != null)
            entity.setPhone(request.phone());

        if (request.email() != null)
            entity.setEmail(request.email());

        if (request.address() != null)
            entity.setAddress(request.address());

        if (request.currency() != null)
            entity.setCurrency(request.currency());

        repository.save(entity);

        return mapper.toResponse(entity);
    }

    // @Override
    // public void delete(UUID id) {

    //     BusinessModel entity = repository.findById(id)
    //             .orElseThrow(() -> new ResourceNotFoundException("Business"));

    //     repository.delete(entity);
    // }
    @Transactional
public void delete(UUID id) {

    BusinessModel business = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

    business.setActive(false);

    repository.save(business);
}
}