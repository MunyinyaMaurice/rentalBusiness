package com.backend.rentalBusiness.assetCategory.entity;

import com.backend.rentalBusiness.business.entity.BusinessModel;
import com.backend.rentalBusiness.core.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "asset_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetCategory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private BusinessModel business;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String icon;

    private Integer sortOrder;

    private Boolean active = true;
}