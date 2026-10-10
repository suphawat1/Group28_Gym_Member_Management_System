package com.gym.membership.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gym.membership.dto.MemberRequestDTO;
import com.gym.membership.dto.MemberResponseDTO;

public interface MemberService {

    MemberResponseDTO createMember(MemberRequestDTO dto);

    MemberResponseDTO getMemberById(Long id);

    List<MemberResponseDTO> getAllMembers();

    Page<MemberResponseDTO> getMembersPage(Pageable pageable);

    MemberResponseDTO updateMember(Long id, MemberRequestDTO dto);

    void deleteMember(Long id);
}