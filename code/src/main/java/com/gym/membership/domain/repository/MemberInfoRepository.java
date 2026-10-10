package com.gym.membership.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.membership.domain.entity.MemberInfo;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long>{
    
}
