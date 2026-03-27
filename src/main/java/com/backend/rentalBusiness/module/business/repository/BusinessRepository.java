package com.backend.rentalBusiness.module.business.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.rentalBusiness.module.business.entity.BusinessModel;

public interface BusinessRepository extends JpaRepository<BusinessModel, UUID> {
    Optional<BusinessModel> findByEmail(String email);
    
}
