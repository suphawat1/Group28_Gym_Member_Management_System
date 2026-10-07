package com.gym.membership.service;

import java.util.List;

import com.gym.membership.dto.MembershipPlanRequestDTO;
import com.gym.membership.dto.MembershipPlanResponseDTO;

public interface MembershipPlanService {

    MembershipPlanResponseDTO createMembershipPlan(MembershipPlanRequestDTO dto);

    MembershipPlanResponseDTO getMembershipPlanById(Long id);

    List<MembershipPlanResponseDTO> getAllMembershipPlans();

    MembershipPlanResponseDTO updateMembershipPlan(Long id, MembershipPlanRequestDTO membershipPlanRequestDTO);

    void deleteMembershipPlan(Long id);
}