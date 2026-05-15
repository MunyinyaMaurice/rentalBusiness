package com.backend.rentalBusiness.asset.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.rentalBusiness.asset.entity.Asset;

public interface AssetRepository extends JpaRepository<Asset, UUID> {
    List<Asset> findByBusinessId(UUID businessId);

    List<Asset> findByStoreId(UUID storeId);
}
