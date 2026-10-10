package com.gym.membership.service;

import java.util.List;

import com.gym.membership.dto.MembershipRequestDTO;
import com.gym.membership.dto.MembershipResponseDTO;

public interface MembershipService {

    MembershipResponseDTO createMembership(MembershipRequestDTO dto);

    MembershipResponseDTO getMembershipById(Long id);

    List<MembershipResponseDTO> getAllMemberships();

    MembershipResponseDTO updateMembership(Long id, MembershipRequestDTO dto);

    void deleteMembership(Long id);
}