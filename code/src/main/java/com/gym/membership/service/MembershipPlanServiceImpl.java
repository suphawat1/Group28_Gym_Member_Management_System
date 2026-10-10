package com.gym.membership.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gym.membership.domain.entity.MembershipPlan;
import com.gym.membership.domain.repository.MembershipPlanRepository;
import com.gym.membership.dto.MembershipPlanRequestDTO;
import com.gym.membership.dto.MembershipPlanResponseDTO;
import com.gym.membership.mapper.MembershipPlanMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembershipPlanServiceImpl implements MembershipPlanService {
    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipPlanMapper membershipPlanMapper;

    @Override
    public MembershipPlanResponseDTO createMembershipPlan(MembershipPlanRequestDTO dto) {
        MembershipPlan membershipPlan = membershipPlanMapper.toEntity(dto);
        MembershipPlan savedMembershipPlan = membershipPlanRepository.save(membershipPlan);
        return membershipPlanMapper.toResponse(savedMembershipPlan);
    }

    @Override
    public MembershipPlanResponseDTO getMembershipPlanById(Long id) {
        MembershipPlan membershipPlan = membershipPlanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MembershipPlan not found"));
        return membershipPlanMapper.toResponse(membershipPlan);
    }

    @Override
    public List<MembershipPlanResponseDTO> getAllMembershipPlans() {
        List<MembershipPlan> membershipPlans = membershipPlanRepository.findAll();
        List<MembershipPlanResponseDTO> result = new ArrayList<>();
        for (MembershipPlan membershipPlan : membershipPlans) {
            result.add(membershipPlanMapper.toResponse(membershipPlan));
        }
        return result;
    }

    @Override
    public MembershipPlanResponseDTO updateMembershipPlan(Long id, MembershipPlanRequestDTO dto) {
        MembershipPlan membershipPlan = membershipPlanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MembershipPlan not found"));
        membershipPlan.setPlanName(dto.getPlanName());
        membershipPlan.setPrice(dto.getPrice());
        membershipPlan.setDurationDays(dto.getDurationDays());

        MembershipPlan savedMembershipPlan = membershipPlanRepository.save(membershipPlan);
        return membershipPlanMapper.toResponse(savedMembershipPlan);
    }

    @Override
    public void deleteMembershipPlan(Long id) {
        MembershipPlan membershipPlan = membershipPlanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MembershipPlan not found"));
        membershipPlanRepository.delete(membershipPlan);
    }
}