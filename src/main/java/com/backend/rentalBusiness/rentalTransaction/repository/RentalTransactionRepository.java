package com.backend.rentalBusiness.rentalTransaction.repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.rentalBusiness.rentalTransaction.entity.RentalTransaction;

public interface RentalTransactionRepository
        extends JpaRepository<RentalTransaction, UUID> {

    List<RentalTransaction> findByBusinessId(UUID businessId);

    List<RentalTransaction> findByStoreId(UUID storeId);

    List<RentalTransaction> findByStatus(String status);

    @Query("""
    SELECT SUM(r.totalAmount)
    FROM RentalTransaction r
    WHERE r.business.id = :businessId
    """)
    BigDecimal totalRevenue(UUID businessId);
    
    @Query("""
SELECT r
FROM RentalTransaction r
WHERE r.status = 'ACTIVE'
AND r.dueDate < :date
""")
List<RentalTransaction> findActiveRentalsBefore(Instant date);
        }