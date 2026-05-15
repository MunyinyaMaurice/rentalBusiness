package com.backend.rentalBusiness.subscription.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.rentalBusiness.subscription.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
     List<Subscription> findByBusinessId(UUID businessId);

}
