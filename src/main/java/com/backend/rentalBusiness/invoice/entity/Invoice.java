package com.backend.rentalBusiness.invoice.entity;

import java.math.BigDecimal;
import java.time.Instant;

import com.backend.rentalBusiness.rentalTransaction.entity.RentalTransaction;
import com.backend.rentalBusiness.core.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "rental_id")
    private RentalTransaction rental;

    private BigDecimal subtotal;

    private BigDecimal tax;

    private BigDecimal lateFee;

    private BigDecimal damageFee;

    private BigDecimal total;

    private String status; // PAID, UNPAID

    private Instant issuedAt;

    private Instant paidAt;
}