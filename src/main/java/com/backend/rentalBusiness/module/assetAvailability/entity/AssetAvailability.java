package com.backend.rentalBusiness.module.assetAvailability.entity;

import com.backend.rentalBusiness.module.asset.entity.Asset;
import com.backend.rentalBusiness.module.util.BaseEntity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;


@Entity
@Table(name = "asset_availability")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetAvailability extends BaseEntity{

    // @Id
    // @GeneratedValue(strategy = GenerationType.UUID)
    // @Column(columnDefinition = "char(36)")
    // private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", referencedColumnName = "id")
    private Asset asset;

    private LocalDate date;

    private Boolean isAvailable;

    private String blockedReason;

    // private Instant createdAt;
}