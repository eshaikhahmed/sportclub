package com.fun.sportclub.service;

import com.fun.sportclub.entity.SubscriptionEntity;
import com.fun.sportclub.entity.UserEntity;
import com.fun.sportclub.repository.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public SubscriptionEntity saveSubscription(SubscriptionEntity subscription){
        return subscriptionRepository.save(subscription);
    }

    public Optional<SubscriptionEntity> getById(Long Id){
        return subscriptionRepository.findById(Id);
    }

    public List<SubscriptionEntity> findAll() {
        return subscriptionRepository.findAll();
    }

    public List<SubscriptionEntity> findManagerApprovals(String status, UserEntity manager){
        return subscriptionRepository.findByStatusAndManager(status, manager);
    }

    public List<SubscriptionEntity> findCurrentUserSubscriptions(UserEntity user){
        return subscriptionRepository.findByUser(user);
    }
}
