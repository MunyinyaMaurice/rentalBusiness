package com.backend.rentalBusiness.module.rentalReturn.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.rentalBusiness.module.rentalReturn.entity.RentalReturn;

public interface RentalReturnRepository extends JpaRepository<RentalReturn, UUID> {
 List<RentalReturn> findByRentalTransactionId(UUID rentalId);   
}
