package com.backend.rentalBusiness.plan.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.rentalBusiness.plan.entity.PlanModel;

public interface PlanRepository extends JpaRepository<PlanModel, UUID> {
    Optional<PlanModel> findByName(String name);
}
