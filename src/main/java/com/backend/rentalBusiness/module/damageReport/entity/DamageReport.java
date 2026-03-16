package com.backend.rentalBusiness.module.damageReport.entity;

import com.backend.rentalBusiness.module.asset.entity.Asset;
import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalTransaction;
import com.backend.rentalBusiness.module.util.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "damage_reports")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DamageReport extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", referencedColumnName = "id")
    private Asset asset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_transaction_id", referencedColumnName = "id")
    private RentalTransaction rentalTransaction;

    private UUID reportedBy;

    private String damageType;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal estimatedRepairCost;

    private BigDecimal actualRepairCost;

    private String repairStatus;

    @Column(columnDefinition = "TEXT")
    private String images;
}