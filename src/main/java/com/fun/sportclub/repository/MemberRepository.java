package com.fun.sportclub.repository;

import com.fun.sportclub.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    public List<MemberEntity> findByStatusAndUserType(String status, String userType);
}