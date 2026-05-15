package com.backend.rentalBusiness.payment.entity;

import com.backend.rentalBusiness.rentalTransaction.entity.RentalTransaction;
import com.backend.rentalBusiness.core.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_transaction_id")
    private RentalTransaction rentalTransaction;

    private BigDecimal amount;

    private String method;
    // CARD, MOBILE_MONEY, BANK_TRANSFER

    private String provider;
    // FLUTTERWAVE

    private String providerTransactionId;

    private String status;
    // PENDING, SUCCESS, FAILED
    private String txRef;
}


// package com.backend.rentalBusiness.payment.entity;

// import com.backend.rentalBusiness.rentalTransaction.entity.RentalTransaction;
// import com.backend.rentalBusiness.core.BaseEntity;

// import jakarta.persistence.*;
// import lombok.*;

// import java.math.BigDecimal;
// import java.time.Instant;
// import java.util.UUID;

// @Entity
// @Table(name = "payments")
// @Getter
// @Setter
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
// public class Payment extends BaseEntity{

//     // @Id
//     // @GeneratedValue(strategy = GenerationType.UUID)
//     // @Column(columnDefinition = "char(36)")
//     // private UUID id;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "rental_transaction_id", referencedColumnName = "id")
//     private RentalTransaction rentalTransaction;

//     private String paymentType;

//     private BigDecimal amount;

//     private String method;

//     private String paymentGateway;

//     private String transactionId;

//     private String status;

//     private BigDecimal refundAmount;

//     private Instant refundDate;

//     private String receiptUrl;

//     // private Instant createdAt;
// }