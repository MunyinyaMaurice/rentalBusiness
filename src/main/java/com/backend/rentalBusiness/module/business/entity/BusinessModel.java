package com.backend.rentalBusiness.module.business.entity;

import com.backend.rentalBusiness.module.asset.entity.Asset;
import com.backend.rentalBusiness.module.assetCategory.entity.AssetCategory;
import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalTransaction;
import com.backend.rentalBusiness.module.store.entity.Store;
import com.backend.rentalBusiness.module.subscription.entity.Subscription;
import com.backend.rentalBusiness.module.util.BaseEntity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.*;


   @Entity
@Table(name = "business")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessModel extends BaseEntity{

    // @Id
    // @GeneratedValue(strategy = GenerationType.UUID)
    // @Column(columnDefinition = "char(36)")
    // private UUID id;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(name = "business_type")
    private String businessType;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    private String address;

    private String timezone;

    private String currency;

    @Column(name = "tax_rate", precision = 10, scale = 2)
    private BigDecimal taxRate;

    @OneToMany(mappedBy = "business")
    private List<Subscription> subscriptions;

    @Column(columnDefinition = "TEXT")
    private String lateFeePolicy;

    @Column(name = "late_fee_per_day")
    @Builder.Default
    private BigDecimal lateFeePerDay = BigDecimal.valueOf(2);

    @Column(columnDefinition = "TEXT")
    private String cancellationPolicy;

    @Column(columnDefinition = "TEXT")
    private String businessHours;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "primary_color")
    private String primaryColor;

    @Column(name = "secondary_color")
    private String secondaryColor;

    @Column(name = "custom_domain")
    private String customDomain;

    @Column(name = "onboarding_completed")
    private Boolean onboardingCompleted = false;

    @Column(name = "overdue_grace_days")
    @Builder.Default
    private Integer overdueGraceDays = 2;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private Set<Store> stores;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private Set<Asset> assets;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private Set<AssetCategory> assetCategories;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private Set<RentalTransaction> rentalTransactions;
}