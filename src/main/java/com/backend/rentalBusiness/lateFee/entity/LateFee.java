package com.backend.rentalBusiness.lateFee.entity;
import com.backend.rentalBusiness.rentalTransaction.entity.RentalTransaction;
import com.backend.rentalBusiness.core.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "late_fees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LateFee extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_transaction_id", nullable = false)
    private RentalTransaction rentalTransaction;

    private Integer daysLate;

    private BigDecimal feePerDay;

    private BigDecimal totalFee;

    private Boolean waived;

    private String waivedReason;

    private Boolean paid;
}