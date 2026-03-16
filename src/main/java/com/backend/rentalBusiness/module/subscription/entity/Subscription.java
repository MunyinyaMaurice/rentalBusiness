package com.backend.rentalBusiness.module.subscription.entity;

import com.backend.rentalBusiness.module.business.entity.BusinessModel;
import com.backend.rentalBusiness.module.plan.entity.PlanModel;
import com.backend.rentalBusiness.module.util.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;


    @Entity
    @Table(name = "subscriptions")
    @Data
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class Subscription extends BaseEntity{

        // @Id
        // @GeneratedValue(strategy = GenerationType.UUID)
        // @Column(columnDefinition = "char(36)")
        // private UUID id;

//        @ManyToOne(fetch = FetchType.LAZY)
//        @JoinColumn(name = "business_id", referencedColumnName = "id", nullable = false)
        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "business_id", referencedColumnName = "id", nullable = false)
        private BusinessModel business;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "plan_id", referencedColumnName = "id", nullable = false)
        private PlanModel planModel;

        private String status;

        private Instant startDate;

        private Instant endDate;

        private Boolean autoRenew;

        // private Instant createdAt;
    }

