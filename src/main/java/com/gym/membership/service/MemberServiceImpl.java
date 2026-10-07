package com.gym.membership.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gym.membership.domain.entity.Member;
import com.gym.membership.domain.entity.MemberInfo;
import com.gym.membership.domain.entity.Trainer;
import com.gym.membership.domain.repository.MemberInfoRepository;
import com.gym.membership.domain.repository.MemberRepository;
import com.gym.membership.domain.repository.TrainerRepository;
import com.gym.membership.dto.MemberRequestDTO;
import com.gym.membership.dto.MemberResponseDTO;
import com.gym.membership.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final MemberMapper memberMapper;

    @Override
    public MemberResponseDTO createMember(MemberRequestDTO dto) {
        Member member = memberMapper.toEntity(dto);   // set ได้แค่ field ธรรมดา

        if (dto.getTrainerId() != null) {             // trainerId ว่างได้
            Trainer trainer = trainerRepository.findById(dto.getTrainerId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));
            member.setTrainer(trainer);               // set FK เอง
        }

        Member savedMember = memberRepository.save(member);
        return memberMapper.toResponse(savedMember);
    }

    @Override
    public MemberResponseDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
        return memberMapper.toResponse(member);
    }

    @Override
    public List<MemberResponseDTO> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        List<MemberResponseDTO> result = new ArrayList<>();
        for (Member member : members) {
            result.add(memberMapper.toResponse(member));
        }
        return result;
    }

    @Override
    public MemberResponseDTO updateMember(Long id, MemberRequestDTO dto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
        member.setUsername(dto.getUsername());
        member.setEmail(dto.getEmail());
        member.setPassword(dto.getPassword());
        member.setPhone(dto.getPhone());

        if (dto.getTrainerId() != null) {
            Trainer trainer = trainerRepository.findById(dto.getTrainerId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));
            member.setTrainer(trainer);
        } else {
            member.setTrainer(null);                  // ไม่ส่ง trainerId = ไม่มีเทรนเนอร์
        }

        Member savedMember = memberRepository.save(member);
        return memberMapper.toResponse(savedMember);
    }

    @Override
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        // ลบ Member ที่ยังมี MemberInfo ผูกอยู่ไม่ได้ (FK) ถ้าไม่เช็กจะได้ 500 จึงตอบ 409 แทน
        for (MemberInfo memberInfo : memberInfoRepository.findAll()) {
            if (memberInfo.getMember().getMemberId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Member still has MemberInfo");
            }
        }

        memberRepository.delete(member);
    }
}
