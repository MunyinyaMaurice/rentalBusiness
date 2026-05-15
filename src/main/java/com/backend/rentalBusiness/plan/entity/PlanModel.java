package com.backend.rentalBusiness.plan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.backend.rentalBusiness.core.BaseEntity;

import java.math.BigDecimal;
@Entity
@Table(name = "plans")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanModel extends BaseEntity{

    // @Id
    // @GeneratedValue(strategy = GenerationType.UUID)
    // @Column(columnDefinition = "char(36)")
    // private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "max_users")
    private Integer maxUsers;

    @Column(name = "max_assets")
    private Integer maxAssets;

    @Column(name = "max_stores")
    private Integer maxStores;

    @JdbcTypeCode(SqlTypes.JSON)
    // @Column(columnDefinition = "json")
    @Column(columnDefinition = "TEXT")
    private String features;

    private Boolean active = true;

    // @CreationTimestamp
    // private Instant createdAt;
}