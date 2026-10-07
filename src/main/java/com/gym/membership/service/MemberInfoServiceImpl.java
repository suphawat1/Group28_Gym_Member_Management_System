package com.gym.membership.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gym.membership.domain.entity.Member;
import com.gym.membership.domain.entity.MemberInfo;
import com.gym.membership.domain.repository.MemberInfoRepository;
import com.gym.membership.domain.repository.MemberRepository;
import com.gym.membership.dto.MemberInfoRequestDTO;
import com.gym.membership.dto.MemberInfoResponseDTO;
import com.gym.membership.mapper.MemberInfoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberInfoServiceImpl implements MemberInfoService {
    private final MemberInfoRepository memberInfoRepository;
    private final MemberRepository memberRepository;
    private final MemberInfoMapper memberInfoMapper;

    @Override
    public MemberInfoResponseDTO createMemberInfo(MemberInfoRequestDTO dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        // One-to-One: 1 Member มีได้แค่ 1 MemberInfo (ไม่มี existsBy... ใน Repository จึงวนเช็กเอง)
        for (MemberInfo existing : memberInfoRepository.findAll()) {
            if (existing.getMember().getMemberId().equals(dto.getMemberId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Member already has MemberInfo");
            }
        }

        MemberInfo memberInfo = memberInfoMapper.toEntity(dto);  // set ได้แค่ field ธรรมดา
        memberInfo.setMember(member);                            // set FK เอง

        MemberInfo savedMemberInfo = memberInfoRepository.save(memberInfo);
        return memberInfoMapper.toResponse(savedMemberInfo);
    }

    @Override
    public MemberInfoResponseDTO getMemberInfoById(Long id) {
        MemberInfo memberInfo = memberInfoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MemberInfo not found"));
        return memberInfoMapper.toResponse(memberInfo);
    }

    @Override
    public List<MemberInfoResponseDTO> getAllMemberInfos() {
        List<MemberInfo> memberInfos = memberInfoRepository.findAll();
        List<MemberInfoResponseDTO> result = new ArrayList<>();
        for (MemberInfo memberInfo : memberInfos) {
            result.add(memberInfoMapper.toResponse(memberInfo));
        }
        return result;
    }

    @Override
    public MemberInfoResponseDTO updateMemberInfo(Long id, MemberInfoRequestDTO dto) {
        MemberInfo memberInfo = memberInfoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MemberInfo not found"));

        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        // เช็ก One-to-One อีกรอบ โดยข้ามตัวเอง (infoId เดียวกัน)
        for (MemberInfo existing : memberInfoRepository.findAll()) {
            if (!existing.getInfoId().equals(id)
                    && existing.getMember().getMemberId().equals(dto.getMemberId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Member already has MemberInfo");
            }
        }

        memberInfo.setMember(member);
        memberInfo.setFullName(dto.getFullName());
        memberInfo.setGender(dto.getGender());
        memberInfo.setDateOfBirth(dto.getDateOfBirth());
        memberInfo.setEmergencyContact(dto.getEmergencyContact());

        MemberInfo savedMemberInfo = memberInfoRepository.save(memberInfo);
        return memberInfoMapper.toResponse(savedMemberInfo);
    }

    @Override
    public void deleteMemberInfo(Long id) {
        MemberInfo memberInfo = memberInfoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MemberInfo not found"));
        memberInfoRepository.delete(memberInfo);
    }
}
