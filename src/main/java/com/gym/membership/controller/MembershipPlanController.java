package com.gym.membership.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gym.membership.dto.MembershipPlanRequestDTO;
import com.gym.membership.dto.MembershipPlanResponseDTO;
import com.gym.membership.service.MembershipPlanService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/membership-plans")
@RequiredArgsConstructor
public class MembershipPlanController {
    private final MembershipPlanService membershipPlanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MembershipPlanResponseDTO createMembershipPlan(@Valid @RequestBody MembershipPlanRequestDTO dto) {
        return membershipPlanService.createMembershipPlan(dto);
    }

    @GetMapping
    public List<MembershipPlanResponseDTO> getAllMembershipPlans() {
        return membershipPlanService.getAllMembershipPlans();
    }

    @GetMapping("/{id}")
    public MembershipPlanResponseDTO getMembershipPlanById(@PathVariable Long id) {
        return membershipPlanService.getMembershipPlanById(id);
    }

    @PutMapping("/{id}")
    public MembershipPlanResponseDTO updateMembershipPlan(@PathVariable Long id, @Valid @RequestBody MembershipPlanRequestDTO dto) {
        return membershipPlanService.updateMembershipPlan(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMembershipPlan(@PathVariable Long id) {
        membershipPlanService.deleteMembershipPlan(id);
    }

}