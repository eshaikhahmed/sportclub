package com.fun.sportclub.repository;

import com.fun.sportclub.entity.MemberEntity;
import com.fun.sportclub.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public List<UserEntity> findByEmail(String email);
    public List<UserEntity> findByUserType(String userType);
}