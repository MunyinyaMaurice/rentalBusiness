package com.backend.rentalBusiness.module.assetCategory.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.rentalBusiness.module.assetCategory.entity.AssetCategory;

public interface AssetCategoryRepository extends JpaRepository<AssetCategory, UUID> {

    List<AssetCategory> findByBusinessId(UUID businessId);

}
