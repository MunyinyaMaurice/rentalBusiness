package com.backend.rentalBusiness.paymentAccount.repository;

import com.backend.rentalBusiness.paymentAccount.entity.PaymentAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentAccountRepository extends JpaRepository<PaymentAccount, UUID> {

    Optional<PaymentAccount> findByBusinessId(UUID businessId);
    

}