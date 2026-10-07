package com.gym.membership.mapper;

import org.springframework.stereotype.Component;

import com.gym.membership.domain.entity.MemberInfo;
import com.gym.membership.dto.MemberInfoRequestDTO;
import com.gym.membership.dto.MemberInfoResponseDTO;

@Component
public class MemberInfoMapper {

    public MemberInfo toEntity(MemberInfoRequestDTO dto) {
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setFullName(dto.getFullName());
        memberInfo.setGender(dto.getGender());
        memberInfo.setDateOfBirth(dto.getDateOfBirth());
        memberInfo.setEmergencyContact(dto.getEmergencyContact());
        return memberInfo;
    }

    public MemberInfoResponseDTO toResponse(MemberInfo memberInfo) {
        MemberInfoResponseDTO memberInfoResponseDTO = new MemberInfoResponseDTO();
        memberInfoResponseDTO.setInfoId(memberInfo.getInfoId());
        memberInfoResponseDTO.setMemberId(memberInfo.getMember().getMemberId());
        memberInfoResponseDTO.setFullName(memberInfo.getFullName());
        memberInfoResponseDTO.setGender(memberInfo.getGender());
        memberInfoResponseDTO.setDateOfBirth(memberInfo.getDateOfBirth());
        memberInfoResponseDTO.setEmergencyContact(memberInfo.getEmergencyContact());
        return memberInfoResponseDTO;
    }
}
