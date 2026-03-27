package com.backend.rentalBusiness.module.business.controller;

import com.backend.rentalBusiness.common.pagination.PageResponse;
import com.backend.rentalBusiness.common.response.ApiResponse;
import com.backend.rentalBusiness.module.business.dto.request.CreateBusinessRequest;
import com.backend.rentalBusiness.module.business.dto.request.UpdateBusinessRequest;
import com.backend.rentalBusiness.module.business.dto.response.BusinessResponse;
import com.backend.rentalBusiness.module.business.service.BusinessService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService service;

    /**
     * Create Business
     */
    @PostMapping
    public ApiResponse<BusinessResponse> createBusiness(
            @Valid @RequestBody CreateBusinessRequest request
    ) {

        BusinessResponse response = service.create(request);

        return ApiResponse.<BusinessResponse>builder()
                .success(true)
                .message("Business created successfully")
                .data(response)
                .build();
    }

    /**
     * Get Business by ID
     */
    @GetMapping("/{id}")
    public ApiResponse<BusinessResponse> getBusiness(
            @PathVariable UUID id
    ) {

        return ApiResponse.<BusinessResponse>builder()
                .success(true)
                .data(service.get(id))
                .build();
    }

    /**
     * List Businesses with Pagination
     */
    @GetMapping
    public PageResponse<BusinessResponse> getBusinesses(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        var result = service.getAll(PageRequest.of(page, size));

        return PageResponse.<BusinessResponse>builder()
                .content(result.getContent())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    /**
     * Update Business
     */
    @PutMapping("/{id}")
    public ApiResponse<BusinessResponse> updateBusiness(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateBusinessRequest request
    ) {

        return ApiResponse.<BusinessResponse>builder()
                .success(true)
                .message("Business updated successfully")
                .data(service.update(id, request))
                .build();
    }

    /**
     * Delete Business
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBusiness(
            @PathVariable UUID id
    ) {

        service.delete(id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Business deleted successfully")
                .build();
    }
}