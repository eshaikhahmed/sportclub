package com.fun.sportclub.service;


import com.fun.sportclub.entity.MemberEntity;
import com.fun.sportclub.repository.MemberRepository;
import com.fun.sportclub.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public MemberEntity saveMember(MemberEntity member){
        return memberRepository.save(member);
    }

    public Optional<MemberEntity> getById(Long Id){
        return memberRepository.findById(Id);
    }

    public List<MemberEntity> findAll() {
        return memberRepository.findAll();
    }

    public List<MemberEntity> findPendingMembers(){
        return memberRepository.findByStatusAndUserType(Constant.MemberStatus.PENDING_ADMIN.toString(), Constant.UserType.MEMBER.toString());
    }
}