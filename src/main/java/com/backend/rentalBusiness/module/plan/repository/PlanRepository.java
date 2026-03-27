package com.backend.rentalBusiness.module.plan.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.rentalBusiness.module.plan.entity.PlanModel;

public interface PlanRepository extends JpaRepository<PlanModel, UUID> {
    Optional<PlanModel> findByName(String name);
}
