package com.backend.rentalBusiness.module.asset.entity;

import com.backend.rentalBusiness.module.assetCategory.entity.AssetCategory;
import com.backend.rentalBusiness.module.business.entity.BusinessModel;
import com.backend.rentalBusiness.module.store.entity.Store;
import com.backend.rentalBusiness.module.util.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "assets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private BusinessModel business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private AssetCategory category;

    @Column(nullable = false)
    private String name;

    private String serialNumber;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal rentalPrice;

    private Integer quantityTotal;

    private Integer quantityAvailable;

    private Integer minRentalPeriod;

    private Integer maxRentalPeriod;

    private Instant purchaseDate;

    private BigDecimal purchasePrice;

    private BigDecimal depreciationRate;

    private BigDecimal currentValue;

    @Column(name = "asset_condition")
    private String condition;

    private String status;

    @Column(columnDefinition = "TEXT")
    private String specifications;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Version
    private Long version;
}