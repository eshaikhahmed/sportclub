package com.fun.sportclub.service;

import com.fun.sportclub.entity.BatchEntity;
import com.fun.sportclub.entity.BatchMemberEntity;
import com.fun.sportclub.entity.SubscriptionEntity;
import com.fun.sportclub.repository.BatchMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BatchMemberService {

    @Autowired
    private BatchMemberRepository batchMemberRepository;

    public BatchMemberEntity saveBatchMember(BatchMemberEntity batchMemberEntity){
        return batchMemberRepository.save(batchMemberEntity);
    }

    public Optional<BatchMemberEntity> getById(Long Id){
        return batchMemberRepository.findById(Id);
    }

    public List<BatchMemberEntity> findAll() {
        return batchMemberRepository.findAll();
    }


    public List<BatchMemberEntity> findBySubscription(SubscriptionEntity subscriptionEntity) {
        return batchMemberRepository.findBySubscription(subscriptionEntity);
    }

    public List<BatchMemberEntity> findByBatch(BatchEntity batch){
        return batchMemberRepository.findByBatch(batch);
    }
}
