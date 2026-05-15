package com.backend.rentalBusiness.store.entity;

import com.backend.rentalBusiness.business.entity.BusinessModel;
import com.backend.rentalBusiness.core.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

    @Entity
    @Table(name = "stores")
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class Store extends BaseEntity{

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "business_id",referencedColumnName = "id", nullable = false)
        private BusinessModel business;

        private String name;

        private String location;

        private String phone;

        private String email;

        @Column(columnDefinition = "TEXT")
        private String address;

     
        @Column(columnDefinition = "TEXT")
    
        private String operatingHours;

        private String status;
    }
