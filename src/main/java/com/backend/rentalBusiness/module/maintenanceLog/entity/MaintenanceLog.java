package com.backend.rentalBusiness.module.maintenanceLog.entity;
import com.backend.rentalBusiness.module.asset.entity.Asset;
import com.backend.rentalBusiness.module.util.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;



@Entity
@Table(name = "maintenance_logs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceLog extends BaseEntity{

    // @Id
    // @GeneratedValue(strategy = GenerationType.UUID)
    // @Column(columnDefinition = "char(36)")
    // private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", referencedColumnName = "id")
    private Asset asset;

    private String maintenanceType;

    private String performedBy;

    private BigDecimal cost;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate nextMaintenanceDate;

    // @JdbcTypeCode(SqlTypes.JSON)
    // @Column(columnDefinition = "json")
    @Column(columnDefinition = "TEXT")
    // private Map<String, Object> attachments;
        private String attachments;

    // private Instant createdAt;
}