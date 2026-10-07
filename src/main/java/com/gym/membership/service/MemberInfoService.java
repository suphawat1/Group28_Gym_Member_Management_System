package com.gym.membership.service;

import java.util.List;

import com.gym.membership.dto.MemberInfoRequestDTO;
import com.gym.membership.dto.MemberInfoResponseDTO;

public interface MemberInfoService {

    MemberInfoResponseDTO createMemberInfo(MemberInfoRequestDTO dto);

    MemberInfoResponseDTO getMemberInfoById(Long id);

    List<MemberInfoResponseDTO> getAllMemberInfos();

    MemberInfoResponseDTO updateMemberInfo(Long id, MemberInfoRequestDTO dto);

    void deleteMemberInfo(Long id);
}
