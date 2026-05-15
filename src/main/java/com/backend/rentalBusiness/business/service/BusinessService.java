package com.backend.rentalBusiness.business.service;

import com.backend.rentalBusiness.business.dto.request.CreateBusinessRequest;
import com.backend.rentalBusiness.business.dto.request.UpdateBusinessRequest;
import com.backend.rentalBusiness.business.dto.response.BusinessResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BusinessService {

    BusinessResponse create(CreateBusinessRequest request);

    BusinessResponse get(UUID id);

    Page<BusinessResponse> getAll(Pageable pageable);

    BusinessResponse update(UUID id, UpdateBusinessRequest request);

    void delete(UUID id);

}