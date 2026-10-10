package com.gym.membership.mapper;

import org.springframework.stereotype.Component;

import com.gym.membership.domain.entity.Member;
import com.gym.membership.dto.MemberRequestDTO;
import com.gym.membership.dto.MemberResponseDTO;

@Component
public class MemberMapper {

    public Member toEntity(MemberRequestDTO dto) {
        Member member = new Member();
        member.setUsername(dto.getUsername());
        member.setEmail(dto.getEmail());
        member.setPassword(dto.getPassword());
        member.setPhone(dto.getPhone());
        return member;
    }

    public MemberResponseDTO toResponse(Member member) {
        MemberResponseDTO memberResponseDTO = new MemberResponseDTO();
        memberResponseDTO.setMemberId(member.getMemberId());
        memberResponseDTO.setUsername(member.getUsername());
        memberResponseDTO.setEmail(member.getEmail());
        memberResponseDTO.setPhone(member.getPhone());
        if (member.getTrainer() != null) {
            memberResponseDTO.setTrainerId(member.getTrainer().getTrainerId());
        }
        return memberResponseDTO;
    }
}
