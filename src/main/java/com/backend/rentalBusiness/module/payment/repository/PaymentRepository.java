package com.backend.rentalBusiness.module.payment.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.rentalBusiness.module.payment.entity.Payment;
import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalTransaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByRentalTransactionId(UUID rentalId);

    Optional<Payment> findByTxRef(String txRef);

    List<Payment> findAllByRentalTransaction_Business_Id(UUID businessId);

}