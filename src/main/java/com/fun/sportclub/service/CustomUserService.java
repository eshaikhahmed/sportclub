package com.fun.sportclub.service;


import com.fun.sportclub.entity.CustomUserDetails;
import com.fun.sportclub.entity.UserEntity;
import com.fun.sportclub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomUserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity saveMember(UserEntity member){
        return userRepository.save(member);
    }

    public Optional<UserEntity> getById(Long Id){
        return userRepository.findById(Id);
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<UserEntity> user = userRepository.findByEmail(username);
        if (user.size() ==  0) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        UserEntity userEntity = user.get(0);

        User userE = new User(userEntity.getEmail(), userEntity.getPassword(), getAuthority(userEntity.getUserType()));
        CustomUserDetails customUserDetail=new CustomUserDetails();
        customUserDetail.setUser(userE);
        customUserDetail.setAuthorities((Set<GrantedAuthority>) userE.getAuthorities());
        customUserDetail.setUserEntity(userEntity);

        return customUserDetail;
    }

    public List<UserEntity> findByUserType(String userType){
        return userRepository.findByUserType(userType);
    }

    private Collection<? extends GrantedAuthority> getAuthority(String role_user) {
        return Collections.singletonList(new SimpleGrantedAuthority(role_user));
    }
}