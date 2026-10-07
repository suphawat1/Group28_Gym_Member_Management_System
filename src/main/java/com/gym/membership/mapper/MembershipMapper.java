package com.gym.membership.mapper;

import org.springframework.stereotype.Component;

import com.gym.membership.domain.entity.Membership;
import com.gym.membership.dto.MembershipRequestDTO;
import com.gym.membership.dto.MembershipResponseDTO;

@Component
public class MembershipMapper {

    public Membership toEntity(MembershipRequestDTO dto) {
        Membership membership = new Membership();
        membership.setStartDate(dto.getStartDate());
        return membership;
    }

    public MembershipResponseDTO toResponse(Membership membership) {
        MembershipResponseDTO membershipResponseDTO = new MembershipResponseDTO();
        membershipResponseDTO.setMembershipId(membership.getMembershipId());
        membershipResponseDTO.setMemberId(membership.getMember().getMemberId());
        membershipResponseDTO.setPlanId(membership.getMembershipPlan().getPlanId());
        membershipResponseDTO.setStartDate(membership.getStartDate());
        membershipResponseDTO.setEndDate(membership.getEndDate());
        return membershipResponseDTO;
    }
}