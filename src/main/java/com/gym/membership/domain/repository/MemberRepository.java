package com.gym.membership.domain.repository;

import com.gym.membership.domain.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    
}
