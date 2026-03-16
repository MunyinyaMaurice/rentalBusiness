// package com.backend.rentalBusiness.module.rentalTransaction.entity;

// import com.backend.rentalBusiness.module.business.entity.BusinessModel;
// import com.backend.rentalBusiness.module.store.entity.Store;
// import com.backend.rentalBusiness.module.util.BaseEntity;

// import jakarta.persistence.*;
// import lombok.*;

// import java.math.BigDecimal;
// import java.time.Instant;
// import java.util.List;

// @Entity
// @Table(name = "rental_transactions")
// @Getter
// @Setter
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
// public class RentalTransaction extends BaseEntity{

//     // @Id
//     // @GeneratedValue(strategy = GenerationType.UUID)
//     // @Column(columnDefinition = "char(36)")
//     // private UUID id;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "business_id", referencedColumnName = "id", nullable = false)
//     private BusinessModel business;

// //    @ManyToOne(fetch = FetchType.LAZY)
// //    private Customer customer;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "store_id", referencedColumnName = "id", nullable = false)
//     private Store store;

//     private Instant rentalDate;

//     private Instant dueDate;

//     private Integer rentalDuration;

//     private BigDecimal subtotal;

//     private BigDecimal discountAmount;

//     private BigDecimal taxAmount;

//     private BigDecimal totalAmount;

//     private BigDecimal paymentAmount;

//     private String status;

//     @OneToMany(mappedBy = "rentalTransaction", cascade = CascadeType.ALL)
//     private List<RentalLine> lines;

//     // private Instant createdAt;
// }


package com.backend.rentalBusiness.module.rentalTransaction.entity;

import com.backend.rentalBusiness.module.business.entity.BusinessModel;
import com.backend.rentalBusiness.module.store.entity.Store;
import com.backend.rentalBusiness.module.util.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "rental_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalTransaction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private BusinessModel business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private Instant rentalDate;

    private Instant dueDate;

    private Integer rentalDuration;

    private BigDecimal subtotal;

    private BigDecimal discountAmount;

    private BigDecimal taxAmount;

    private BigDecimal totalAmount;

    private BigDecimal paymentAmount;

    @Enumerated(EnumType.STRING)
    private RentalStatus status;

    @OneToMany(mappedBy = "rentalTransaction", cascade = CascadeType.ALL)
    private List<RentalLine> lines;

    
}