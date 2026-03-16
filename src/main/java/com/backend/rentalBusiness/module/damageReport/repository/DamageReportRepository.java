package com.backend.rentalBusiness.module.damageReport.repository;

import com.backend.rentalBusiness.module.damageReport.entity.DamageReport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DamageReportRepository extends JpaRepository<DamageReport, UUID> {

    List<DamageReport> findByAssetId(UUID assetId);

    List<DamageReport> findByRentalTransactionId(UUID rentalTransactionId);

    List<DamageReport> findByRepairStatus(String repairStatus);

}