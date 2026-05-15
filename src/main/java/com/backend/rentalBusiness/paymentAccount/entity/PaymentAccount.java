package com.backend.rentalBusiness.paymentAccount.entity;

import com.backend.rentalBusiness.business.entity.BusinessModel;
import com.backend.rentalBusiness.core.BaseEntity;
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

    @Column(name = "subaccount_id")
private String subaccountId;

@Column(name = "bank_name")
private String bankName;

@Column(name = "account_number")
private String accountNumber;

@Column(name = "split_ratio")
private Double splitRatio; // e.g. 0.9 = 90%
}