package com.fun.sportclub.repository;

import com.fun.sportclub.entity.SubscriptionEntity;
import com.fun.sportclub.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    public List<SubscriptionEntity> findByStatusAndManager(String status, UserEntity manager);
    public List<SubscriptionEntity> findByUser(UserEntity user);
}