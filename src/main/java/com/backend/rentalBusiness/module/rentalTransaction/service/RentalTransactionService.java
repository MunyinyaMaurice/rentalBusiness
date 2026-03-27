package com.backend.rentalBusiness.module.rentalTransaction.service;

import java.util.*;

import com.backend.rentalBusiness.module.rentalTransaction.dto.request.CreateRentalTransactionRequest;
import com.backend.rentalBusiness.module.rentalTransaction.dto.response.RemainingAssetResponse;
import com.backend.rentalBusiness.module.rentalTransaction.dto.response.RentalSummaryResponse;
import com.backend.rentalBusiness.module.rentalTransaction.dto.response.RentalTimelineResponse;
import com.backend.rentalBusiness.module.rentalTransaction.dto.response.RentalTransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RentalTransactionService {

    RentalTransactionResponse create(CreateRentalTransactionRequest request);

    RentalTransactionResponse get(UUID id);

    List<RentalTransactionResponse> getByBusiness(UUID businessId);
    List<RentalTransactionResponse> getByStore(UUID storeId);
    List<RentalTransactionResponse> getByStatus(String status);
    void cancel(UUID rentalId);
    void markReturned(UUID rentalId);
    Page<RentalTransactionResponse> getAll(Pageable pageable);
    List<RemainingAssetResponse> getRemainingAssets(UUID rentalId);
    RentalSummaryResponse getRentalSummary(UUID rentalId);
    List<RentalTimelineResponse> getTimeline(UUID rentalId);
}