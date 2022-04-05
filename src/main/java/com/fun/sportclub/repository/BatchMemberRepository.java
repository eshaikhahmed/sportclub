package com.fun.sportclub.repository;

import com.fun.sportclub.entity.BatchEntity;
import com.fun.sportclub.entity.BatchMemberEntity;
import com.fun.sportclub.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchMemberRepository extends JpaRepository<BatchMemberEntity, Long> {
    List<BatchMemberEntity> findBySubscription(SubscriptionEntity subscription);
    List<BatchMemberEntity> findByBatch(BatchEntity batch);
}