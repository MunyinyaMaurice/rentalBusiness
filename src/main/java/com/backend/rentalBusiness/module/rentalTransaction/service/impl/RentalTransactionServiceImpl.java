package com.backend.rentalBusiness.module.rentalTransaction.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.backend.rentalBusiness.module.asset.entity.Asset;
import com.backend.rentalBusiness.module.asset.repository.AssetRepository;
import com.backend.rentalBusiness.module.business.repository.BusinessRepository;
import com.backend.rentalBusiness.module.damageReport.entity.DamageReport;
import com.backend.rentalBusiness.module.damageReport.repository.DamageReportRepository;
import com.backend.rentalBusiness.module.lateFee.entity.LateFee;
import com.backend.rentalBusiness.module.lateFee.repository.LateFeeRepository;
import com.backend.rentalBusiness.module.rentalReturn.entity.RentalReturn;
import com.backend.rentalBusiness.module.rentalReturn.repository.RentalReturnRepository;
import com.backend.rentalBusiness.module.rentalTransaction.dto.request.CreateRentalLineRequest;
import com.backend.rentalBusiness.module.rentalTransaction.dto.request.CreateRentalTransactionRequest;
import com.backend.rentalBusiness.module.rentalTransaction.dto.response.RemainingAssetResponse;
import com.backend.rentalBusiness.module.rentalTransaction.dto.response.RentalSummaryResponse;
import com.backend.rentalBusiness.module.rentalTransaction.dto.response.RentalTimelineResponse;
import com.backend.rentalBusiness.module.rentalTransaction.dto.response.RentalTransactionResponse;
import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalLine;
import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalStatus;
import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalTransaction;
import com.backend.rentalBusiness.module.rentalTransaction.mapper.RentalTransactionMapper;
import com.backend.rentalBusiness.module.rentalTransaction.repository.RentalTransactionRepository;
import com.backend.rentalBusiness.module.rentalTransaction.service.RentalTransactionService;
import com.backend.rentalBusiness.module.store.repository.StoreRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class RentalTransactionServiceImpl
        implements RentalTransactionService {

    private final RentalTransactionRepository repository;
    private final AssetRepository assetRepository;
    private final BusinessRepository businessRepository;
    private final StoreRepository storeRepository;
    private final RentalTransactionMapper mapper;
    private final DamageReportRepository damageReportRepository;
    private final LateFeeRepository lateFeeRepository;
    private final RentalReturnRepository returnRepository;

    @Override
    @Transactional
    public RentalTransactionResponse create(CreateRentalTransactionRequest request) {

        RentalTransaction transaction = new RentalTransaction();

        transaction.setBusiness(
                businessRepository.findById(request.businessId())
                        .orElseThrow(() -> new RuntimeException("Business not found"))
        );

        transaction.setStore(
                storeRepository.findById(request.storeId())
                        .orElseThrow(() -> new RuntimeException("Store not found"))
        );

        transaction.setRentalDate(request.rentalDate());
        transaction.setDueDate(request.dueDate());
        transaction.setRentalDuration(request.rentalDuration());
        transaction.setStatus(RentalStatus.ACTIVE);

        List<RentalLine> lines = new ArrayList<>();

        BigDecimal subtotal = BigDecimal.ZERO;

        for (CreateRentalLineRequest lineReq : request.lines()) {

            Asset asset = assetRepository.findById(lineReq.assetId())
                    .orElseThrow(() -> new RuntimeException("Asset not found"));

            if (asset.getQuantityAvailable() < lineReq.quantity()) {
                throw new RuntimeException("Not enough stock for asset: " + asset.getName());
            }

            asset.setQuantityAvailable(
                    asset.getQuantityAvailable() - lineReq.quantity()
            );

            assetRepository.save(asset);

            BigDecimal lineTotal =
                    lineReq.price().multiply(BigDecimal.valueOf(lineReq.quantity()));

            RentalLine line = RentalLine.builder()
                    .asset(asset)
                    .rentalTransaction(transaction)
                    .priceAtRent(lineReq.price())
                    .quantity(lineReq.quantity())
                    .lineTotal(lineTotal)
                    .build();

            lines.add(line);

            subtotal = subtotal.add(lineTotal);
        }

        // BUSINESS CALCULATION LOGIC

        // BigDecimal tax = subtotal.multiply(new BigDecimal("0.18"));
        BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(0.18))
        .setScale(2, RoundingMode.HALF_UP);

        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal total = subtotal.add(tax).subtract(discount);

        transaction.setSubtotal(subtotal);
        transaction.setTaxAmount(tax);
        transaction.setDiscountAmount(discount);
        transaction.setTotalAmount(total);
        transaction.setPaymentAmount(total);

        transaction.setLines(lines);

        repository.save(transaction);

        return mapper.toResponse(transaction);
    }
    @Override
public RentalTransactionResponse get(UUID id) {

    RentalTransaction transaction = repository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                    "Rental transaction not found: " + id
            ));

    return mapper.toResponse(transaction);
}

   @Override
public List<RentalTransactionResponse> getByBusiness(UUID businessId) {

    return repository.findByBusinessId(businessId)
            .stream()
            .map(mapper::toResponse)
            .toList();
}
   @Override
public List<RentalTransactionResponse> getByStore(UUID storeId) {

    return repository.findByStoreId(storeId)
            .stream()
            .map(mapper::toResponse)
            .toList();
}
@Override
public List<RentalTransactionResponse> getByStatus(String status) {

    return repository.findByStatus(status)
            .stream()
            .map(mapper::toResponse)
            .toList();
}
@Override
@Transactional
public void cancel(UUID rentalId) {

    RentalTransaction transaction = repository.findById(rentalId)
            .orElseThrow(() -> new RuntimeException("Rental not found"));

    if (!"ACTIVE".equals(transaction.getStatus())) {
        throw new RuntimeException("Only ACTIVE rentals can be cancelled");
    }

    transaction.setStatus(RentalStatus.CANCELLED);

    // restore stock
    for (RentalLine line : transaction.getLines()) {

        Asset asset = line.getAsset();

        asset.setQuantityAvailable(
                asset.getQuantityAvailable() + line.getQuantity()
        );

        assetRepository.save(asset);
    }

    repository.save(transaction);
}
@Override
@Transactional
public void markReturned(UUID rentalId) {

    RentalTransaction transaction = repository.findById(rentalId)
            .orElseThrow(() -> new RuntimeException("Rental not found"));

    transaction.setStatus(RentalStatus.RETURNED);

    for (RentalLine line : transaction.getLines()) {

        Asset asset = line.getAsset();

        asset.setQuantityAvailable(
                asset.getQuantityAvailable() + line.getQuantity()
        );

        assetRepository.save(asset);
    }

    repository.save(transaction);
}
@Override
public Page<RentalTransactionResponse> getAll(Pageable pageable) {

    return repository.findAll(pageable)
            .map(mapper::toResponse);
}
@Override
public List<RemainingAssetResponse> getRemainingAssets(UUID rentalId) {

    RentalTransaction rental =
            repository.findById(rentalId)
                    .orElseThrow(() ->
                            new RuntimeException("Rental not found"));

    return rental.getLines()
            .stream()
            .map(line -> {

                int returned =
                        line.getReturnedQuantity() == null
                                ? 0
                                : line.getReturnedQuantity();

                int remaining =
                        line.getQuantity() - returned;

                return new RemainingAssetResponse(
                        line.getAsset().getId(),
                        line.getQuantity(),
                        returned,
                        remaining
                );

            })
            .toList();
}

@Override
public RentalSummaryResponse getRentalSummary(UUID rentalId) {

    RentalTransaction rental =
            repository.findById(rentalId)
                    .orElseThrow(() ->
                            new RuntimeException("Rental not found"));

    int total = 0;
    int returned = 0;

    for (RentalLine line : rental.getLines()) {

        int returnedQty =
                line.getReturnedQuantity() == null
                        ? 0
                        : line.getReturnedQuantity();

        total += line.getQuantity();
        returned += returnedQty;
    }

    int remaining = total - returned;

    boolean overdue = Instant.now().isAfter(rental.getDueDate());

    boolean fullyReturned = remaining == 0;

    return new RentalSummaryResponse(
            rental.getId(),
            total,
            returned,
            remaining,
            fullyReturned,
                overdue,
            rental.getStatus()
    );
}
@Override
public List<RentalTimelineResponse> getTimeline(UUID rentalId) {

    RentalTransaction rental =
            repository.findById(rentalId)
                    .orElseThrow(() ->
                            new RuntimeException("Rental not found"));

    List<RentalTimelineResponse> timeline = new ArrayList<>();

    // Rental created
    timeline.add(new RentalTimelineResponse(
            rental.getCreatedAt(),
            "RENTAL_CREATED",
            "Rental transaction created"
    ));

    // Returns
    List<RentalReturn> returns =
            returnRepository.findByRentalTransactionId(rentalId);

    for (RentalReturn ret : returns) {

        timeline.add(new RentalTimelineResponse(
                ret.getReturnDate(),
                "ASSET_RETURNED",
                "Asset returned: " + ret.getAsset().getName()
        ));
    }

    // Damage reports
    List<DamageReport> damages =
            damageReportRepository.findByRentalTransactionId(rentalId);

    for (DamageReport damage : damages) {

        timeline.add(new RentalTimelineResponse(
                damage.getCreatedAt(),
                "DAMAGE_REPORTED",
                "Damage reported: " + damage.getDamageType()
        ));
    }

    // Late fees
    List<LateFee> fees =
            lateFeeRepository.findByRentalTransactionId(rentalId);

    for (LateFee fee : fees) {

        timeline.add(new RentalTimelineResponse(
                fee.getCreatedAt(),
                "LATE_FEE_APPLIED",
                "Late fee applied: " + fee.getTotalFee()
        ));
    }

    // Rental completed
    if ("RETURNED".equals(rental.getStatus().name())) {

        timeline.add(new RentalTimelineResponse(
                rental.getUpdatedAt(),
                "RENTAL_COMPLETED",
                "All assets returned"
        ));
    }

    // Sort timeline by timestamp
    timeline.sort(Comparator.comparing(RentalTimelineResponse::timestamp));

    return timeline;
}
}