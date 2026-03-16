package com.backend.rentalBusiness.module.rentalTransaction.repository;

import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RentalLineRepository extends JpaRepository<RentalLine, UUID> {

    Optional<RentalLine> findByRentalTransactionIdAndAssetId(
            UUID rentalTransactionId,
            UUID assetId
    );

}