package com.PMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PMS.model.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findByUserId(Long userId);
    
}
