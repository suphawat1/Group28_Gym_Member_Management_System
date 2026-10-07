package com.gym.membership.mapper;
import org.springframework.stereotype.Component;

import com.gym.membership.domain.entity.MembershipPlan;
import com.gym.membership.dto.MembershipPlanRequestDTO;
import com.gym.membership.dto.MembershipPlanResponseDTO;
@Component
public class MembershipPlanMapper {

    public MembershipPlan toEntity(MembershipPlanRequestDTO dto) {
        MembershipPlan membershipPlan = new MembershipPlan();
        membershipPlan.setPlanName(dto.getPlanName());
        membershipPlan.setPrice(dto.getPrice());
        membershipPlan.setDurationDays(dto.getDurationDays());
        return membershipPlan;
    }

    public MembershipPlanResponseDTO toResponse(MembershipPlan membershipPlan) {
        MembershipPlanResponseDTO membershipPlanResponseDTO = new MembershipPlanResponseDTO();
        membershipPlanResponseDTO.setPlanId(membershipPlan.getPlanId());
        membershipPlanResponseDTO.setPlanName(membershipPlan.getPlanName());
        membershipPlanResponseDTO.setPrice(membershipPlan.getPrice());
        membershipPlanResponseDTO.setDurationDays(membershipPlan.getDurationDays());
        return membershipPlanResponseDTO;
    }
}