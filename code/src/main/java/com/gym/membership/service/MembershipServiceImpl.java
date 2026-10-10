package com.gym.membership.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gym.membership.domain.entity.Member;
import com.gym.membership.domain.entity.Membership;
import com.gym.membership.domain.entity.MembershipPlan;
import com.gym.membership.domain.repository.MemberRepository;
import com.gym.membership.domain.repository.MembershipPlanRepository;
import com.gym.membership.domain.repository.MembershipRepository;
import com.gym.membership.dto.MembershipRequestDTO;
import com.gym.membership.dto.MembershipResponseDTO;
import com.gym.membership.mapper.MembershipMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {
    private final MembershipRepository membershipRepository;
    private final MemberRepository memberRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipMapper membershipMapper;

    @Override
    public MembershipResponseDTO createMembership(MembershipRequestDTO dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
        MembershipPlan plan = membershipPlanRepository.findById(dto.getPlanId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found"));

        Membership membership = membershipMapper.toEntity(dto);
        membership.setMember(member);
        membership.setMembershipPlan(plan);
        membership.setEndDate(dto.getStartDate().plusDays(plan.getDurationDays()));

        Membership savedMembership = membershipRepository.save(membership);
        return membershipMapper.toResponse(savedMembership);
    }

    @Override
    public MembershipResponseDTO getMembershipById(Long id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membership not found"));
        return membershipMapper.toResponse(membership);
    }

    @Override
    public List<MembershipResponseDTO> getAllMemberships() {
        List<Membership> memberships = membershipRepository.findAll();
        List<MembershipResponseDTO> result = new ArrayList<>();
        for (Membership membership : memberships) {
            result.add(membershipMapper.toResponse(membership));
        }
        return result;
    }

    @Override
    public MembershipResponseDTO updateMembership(Long id, MembershipRequestDTO dto) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membership not found"));
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
        MembershipPlan plan = membershipPlanRepository.findById(dto.getPlanId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found"));

        membership.setMember(member);
        membership.setMembershipPlan(plan);
        membership.setStartDate(dto.getStartDate());
        membership.setEndDate(dto.getStartDate().plusDays(plan.getDurationDays()));

        Membership savedMembership = membershipRepository.save(membership);
        return membershipMapper.toResponse(savedMembership);
    }

    @Override
    public void deleteMembership(Long id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membership not found"));
        membershipRepository.delete(membership);
    }
}