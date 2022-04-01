package com.fun.sportclub.repository;

import com.fun.sportclub.entity.SportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SportRepository extends JpaRepository<SportEntity, Long> {

}