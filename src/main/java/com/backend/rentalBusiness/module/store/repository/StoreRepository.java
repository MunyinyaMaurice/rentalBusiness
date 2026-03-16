package com.backend.rentalBusiness.module.store.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.rentalBusiness.module.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, UUID> {
     List<Store> findByBusinessId(UUID businessId);

}
