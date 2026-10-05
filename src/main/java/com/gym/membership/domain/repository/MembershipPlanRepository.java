package com.gym.membership.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.membership.domain.entity.MembershipPlan;

public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {
    
}
