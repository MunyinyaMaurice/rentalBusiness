package com.backend.rentalBusiness.module.invoice.entity;

import java.math.BigDecimal;

import com.backend.rentalBusiness.module.util.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invoice_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    private String description;

    private BigDecimal amount;
}