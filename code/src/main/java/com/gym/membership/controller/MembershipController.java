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

import com.gym.membership.dto.MembershipRequestDTO;
import com.gym.membership.dto.MembershipResponseDTO;
import com.gym.membership.service.MembershipService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/memberships")
@RequiredArgsConstructor
public class MembershipController {
    private final MembershipService membershipService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MembershipResponseDTO createMembership(@Valid @RequestBody MembershipRequestDTO dto) {
        return membershipService.createMembership(dto);
    }

    @GetMapping
    public List<MembershipResponseDTO> getAllMemberships() {
        return membershipService.getAllMemberships();
    }

    @GetMapping("/{id}")
    public MembershipResponseDTO getMembershipById(@PathVariable Long id) {
        return membershipService.getMembershipById(id);
    }

    @PutMapping("/{id}")
    public MembershipResponseDTO updateMembership(@PathVariable Long id, @Valid @RequestBody MembershipRequestDTO dto) {
        return membershipService.updateMembership(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMembership(@PathVariable Long id) {
        membershipService.deleteMembership(id);
    }
}