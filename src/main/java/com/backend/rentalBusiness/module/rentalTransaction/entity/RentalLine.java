package com.backend.rentalBusiness.module.rentalTransaction.entity;

import com.backend.rentalBusiness.module.asset.entity.Asset;
import com.backend.rentalBusiness.module.util.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "rental_lines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalLine extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_transaction_id")
    private RentalTransaction rentalTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    private Asset asset;

    private BigDecimal priceAtRent;

    private Integer quantity;

    @Builder.Default
    private Integer returnedQuantity = 0;

    private BigDecimal lineTotal;
}


// package com.backend.rentalBusiness.module.rentalTransaction.entity;

// import com.backend.rentalBusiness.module.asset.entity.Asset;
// import com.backend.rentalBusiness.module.util.BaseEntity;

// import jakarta.persistence.*;
// import lombok.*;

// import java.math.BigDecimal;

// @Entity
// @Table(name = "rental_lines")
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class RentalLine extends BaseEntity {

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "rental_transaction_id")
//     private RentalTransaction rentalTransaction;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "asset_id")
//     private Asset asset;

//     private BigDecimal priceAtRent;

//     private Integer quantity;

//     private BigDecimal lineTotal;
// }