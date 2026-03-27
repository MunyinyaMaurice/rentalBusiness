package com.backend.rentalBusiness.module.rentalReturn.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.springframework.stereotype.Service;

import com.backend.rentalBusiness.module.asset.entity.Asset;
import com.backend.rentalBusiness.module.asset.repository.AssetRepository;
import com.backend.rentalBusiness.module.damageReport.entity.DamageReport;
import com.backend.rentalBusiness.module.damageReport.repository.DamageReportRepository;
import com.backend.rentalBusiness.module.lateFee.service.LateFeeService;
import com.backend.rentalBusiness.module.rentalReturn.dto.request.ReturnAssetRequest;
import com.backend.rentalBusiness.module.rentalReturn.dto.response.ReturnAssetResponse;
import com.backend.rentalBusiness.module.rentalReturn.entity.RentalReturn;
import com.backend.rentalBusiness.module.rentalReturn.mapper.RentalReturnMapper;
import com.backend.rentalBusiness.module.rentalReturn.repository.RentalReturnRepository;
import com.backend.rentalBusiness.module.rentalReturn.service.RentalReturnService;
import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalLine;
import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalStatus;
import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalTransaction;
import com.backend.rentalBusiness.module.rentalTransaction.repository.RentalLineRepository;
import com.backend.rentalBusiness.module.rentalTransaction.repository.RentalTransactionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RentalReturnServiceImpl implements RentalReturnService {

    private final RentalReturnRepository returnRepository;
    private final RentalTransactionRepository rentalRepository;
    private final RentalLineRepository rentalLineRepository;
    private final AssetRepository assetRepository;
    private final RentalReturnMapper mapper;
    private final DamageReportRepository damageReportRepository;
    private final LateFeeService lateFeeService;

    @Transactional
public ReturnAssetResponse returnAsset(ReturnAssetRequest request) {

    RentalTransaction rental =
            rentalRepository.findById(request.rentalTransactionId())
                    .orElseThrow();

    RentalLine line =
            rentalLineRepository.findByRentalTransactionIdAndAssetId(
                    request.rentalTransactionId(),
                    request.assetId()
            ).orElseThrow();

    int remaining =
            line.getQuantity() - line.getReturnedQuantity();

    if (request.quantity() > remaining) {
        throw new RuntimeException("Return quantity exceeds rented quantity");
    }
    // update returned quantity
    line.setReturnedQuantity(
            line.getReturnedQuantity() + request.quantity()
    );

    rentalLineRepository.save(line);

    // restore inventory
    Asset asset = line.getAsset();

    asset.setQuantityAvailable(
            asset.getQuantityAvailable() + request.quantity()
    );

    assetRepository.save(asset);

    Instant now = Instant.now();

    boolean late = now.isAfter(rental.getDueDate());

    int daysLate = late ?
            (int) ChronoUnit.DAYS.between(rental.getDueDate(), now) : 0;

//     BigDecimal lateFee =
//             BigDecimal.valueOf(daysLate).multiply(new BigDecimal("2"));
        BigDecimal lateFee = lateFeeService.calculateLateFee(rental);

    if ("DAMAGED".equalsIgnoreCase(request.conditionOnReturn())) {

        DamageReport report = DamageReport.builder()
                .asset(asset)
                .rentalTransaction(rental)
                .damageType(request.damageType())
                .description(request.damageDescription())
                .estimatedRepairCost(request.estimatedRepairCost())
                .repairStatus("PENDING")
                .build();

        damageReportRepository.save(report);
    }
    // create return record
    RentalReturn ret = RentalReturn.builder()
            .rentalTransaction(rental)
            .asset(asset)
            .returnDate(now)
            .lateReturn(late)
            .daysLate(daysLate)
            .lateFee(lateFee)
            .conditionOnReturn(request.conditionOnReturn())
            .damageNotes(request.damageDescription())
            .damageCharge(request.estimatedRepairCost())
            .build();


        returnRepository.save(ret);

        // CHECK IF RENTAL FULLY RETURNED
        boolean fullyReturned =
                rental.getLines()
                        .stream()
                        .allMatch(lineItem ->
                                lineItem.getReturnedQuantity() >= lineItem.getQuantity()
                        );

        if (fullyReturned) {
        rental.setStatus(RentalStatus.RETURNED);
        rentalRepository.save(rental);
        }

        return mapper.toResponse(ret);
}

//     @Transactional
// public ReturnAssetResponse returnAsset(ReturnAssetRequest request) {

//     RentalTransaction rental =
//             rentalRepository.findById(request.rentalTransactionId())
//                     .orElseThrow();

//     RentalLine line =
//             rentalLineRepository.findByRentalTransactionIdAndAssetId(
//                     request.rentalTransactionId(),
//                     request.assetId()
//             ).orElseThrow();

//     int rentedQty = line.getQuantity();
//     int returnedQty = line.getReturnedQuantity();
//     int remainingQty = rentedQty - returnedQty;

//     if (request.quantity() > remainingQty) {
//         throw new RuntimeException("Return quantity exceeds remaining rented items");
//     }

//     // update returned quantity
//     line.setReturnedQuantity(returnedQty + request.quantity());

//     rentalLineRepository.save(line);

//     // restore inventory
//     Asset asset = line.getAsset();

//     asset.setQuantityAvailable(
//             asset.getQuantityAvailable() + request.quantity()
//     );

//     assetRepository.save(asset);

//     // create return record
//     RentalReturn ret = RentalReturn.builder()
//             .rentalTransaction(rental)
//             .asset(asset)
//             .returnDate(Instant.now())
//             .build();

//     returnRepository.save(ret);

//     // check if rental fully returned
//     boolean fullyReturned =
//             rental.getLines()
//                     .stream()
//                     .allMatch(l ->
//                             l.getReturnedQuantity() >= l.getQuantity()
//                     );

//     if (fullyReturned) {
//         rental.setStatus(RentalStatus.RETURNED);
//         rentalRepository.save(rental);
//     }

//     return mapper.toResponse(ret);
// }
@Override
public List<ReturnAssetResponse> getReturnsByRental(UUID rentalId) {

    if (!rentalRepository.existsById(rentalId)) {
        throw new RuntimeException("Rental transaction not found");
    }

    return returnRepository.findByRentalTransactionId(rentalId)
            .stream()
            .map(mapper::toResponse)
            .toList();
}
}