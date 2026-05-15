package com.backend.rentalBusiness.lateFee.repository;
import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.rentalBusiness.lateFee.entity.LateFee;

@Repository
public interface LateFeeRepository extends JpaRepository<LateFee, UUID> {

    List<LateFee> findByRentalTransactionId(UUID rentalTransactionId);
    // Optional<LateFee> findByRentalTransactionId(UUID rentalId);
}