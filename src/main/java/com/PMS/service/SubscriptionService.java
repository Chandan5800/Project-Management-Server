package com.PMS.service;

import com.PMS.model.PlanType;
import com.PMS.model.Subscription;
import com.PMS.model.User;

public interface SubscriptionService {
    Subscription createSubscription(User user);

    Subscription getUserSubscription(Long userId) throws Exception;

    Subscription upgradeSubscription(Long userId, PlanType planType) throws Exception;

    boolean isValid(Subscription subscription);
}
