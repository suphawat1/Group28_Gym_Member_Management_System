package com.gym.membership.service;

import java.util.List;

import com.gym.membership.dto.MemberRequestDTO;
import com.gym.membership.dto.MemberResponseDTO;

public interface MemberService {

    MemberResponseDTO createMember(MemberRequestDTO dto);

    MemberResponseDTO getMemberById(Long id);

    List<MemberResponseDTO> getAllMembers();

    MemberResponseDTO updateMember(Long id, MemberRequestDTO dto);

    void deleteMember(Long id);
}
