package com.backend.rentalBusiness.module.rentalTransaction.controller;

import com.backend.rentalBusiness.module.rentalTransaction.dto.request.CreateRentalTransactionRequest;
import com.backend.rentalBusiness.module.rentalTransaction.dto.response.*;
import com.backend.rentalBusiness.module.rentalTransaction.service.RentalTransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.data.domain.*;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalTransactionController {

    private final RentalTransactionService service;

    /**
     * Create new rental transaction
     */
    @PostMapping
    public RentalTransactionResponse create(
            @Valid @RequestBody CreateRentalTransactionRequest request
    ) {
        return service.create(request);
    }

    /**
     * Get rental transaction by ID
     */
    @GetMapping("/{id}")
    public RentalTransactionResponse get(@PathVariable UUID id) {
        return service.get(id);
    }

    /**
     * Get all rentals for a business
     */
    @GetMapping("/business/{businessId}")
    public List<RentalTransactionResponse> getByBusiness(
            @PathVariable UUID businessId
    ) {
        return service.getByBusiness(businessId);
    }

    @GetMapping("/store/{storeId}")
    public List<RentalTransactionResponse> getByStore(@PathVariable UUID storeId) {
    return service.getByStore(storeId);
    }

    @GetMapping("/status/{status}")
    public List<RentalTransactionResponse> getByStatus(@PathVariable String status) {
    return service.getByStatus(status);
}

    @PostMapping("/{id}/cancel")
    public void cancel(@PathVariable UUID id) {
    service.cancel(id);
}
    @GetMapping
    public Page<RentalTransactionResponse> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @GetMapping("/{rentalId}/remaining")
public List<RemainingAssetResponse> getRemainingAssets(
        @PathVariable UUID rentalId) {

    return service.getRemainingAssets(rentalId);
}
@GetMapping("/{rentalId}/summary")
public RentalSummaryResponse getRentalSummary(
        @PathVariable UUID rentalId) {

    return service.getRentalSummary(rentalId);
}
@GetMapping("/{rentalId}/timeline")
public List<RentalTimelineResponse> getTimeline(
        @PathVariable UUID rentalId) {

    return service.getTimeline(rentalId);
}

}