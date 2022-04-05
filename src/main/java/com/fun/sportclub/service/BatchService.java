package com.fun.sportclub.service;

import com.fun.sportclub.entity.BatchEntity;
import com.fun.sportclub.repository.BatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BatchService {

    @Autowired
    private BatchRepository batchRepository;

    public BatchEntity saveBatch(BatchEntity subscription){
        return batchRepository.save(subscription);
    }

    public Optional<BatchEntity> getById(Long Id){
        return batchRepository.findById(Id);
    }

    public List<BatchEntity> findAll() {
        return batchRepository.findAll();
    }


}
