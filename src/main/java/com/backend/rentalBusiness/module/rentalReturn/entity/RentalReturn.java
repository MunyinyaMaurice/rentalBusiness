package com.backend.rentalBusiness.module.rentalReturn.entity;

import com.backend.rentalBusiness.module.asset.entity.Asset;
import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalTransaction;
import com.backend.rentalBusiness.module.util.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "returns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalReturn extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_transaction_id", nullable = false)
    private RentalTransaction rentalTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    private Instant returnDate;

    private String conditionOnReturn;

    @Column(columnDefinition = "TEXT")
    private String damageNotes;

    private BigDecimal damageCharge;

    private Boolean lateReturn;

    private Integer daysLate;

    private BigDecimal lateFee;

    private String status;
    @Column(name = "returned_quantity")
    @Builder.Default
    private Integer returnedQuantity = 0;
}
