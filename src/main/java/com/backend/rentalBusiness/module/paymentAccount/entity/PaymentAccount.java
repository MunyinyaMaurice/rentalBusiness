package com.backend.rentalBusiness.module.paymentAccount.entity;

import com.backend.rentalBusiness.module.business.entity.BusinessModel;
import com.backend.rentalBusiness.module.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "business_payment_accounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAccount extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private BusinessModel business;

    private String provider;
    // STRIPE, FLUTTERWAVE, PAYSTACK

    private String providerAccountId;
    // acct_12345

    private String country;

    private String currency;

    private Boolean payoutsEnabled;

    private Boolean detailsSubmitted;
}