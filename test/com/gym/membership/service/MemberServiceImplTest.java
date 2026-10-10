package com.gym.membership.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.gym.membership.domain.entity.Member;
import com.gym.membership.domain.entity.MemberInfo;
import com.gym.membership.domain.entity.Membership;
import com.gym.membership.domain.entity.Trainer;
import com.gym.membership.domain.entity.TrainingSession;
import com.gym.membership.domain.repository.MemberInfoRepository;
import com.gym.membership.domain.repository.MemberRepository;
import com.gym.membership.domain.repository.MembershipRepository;
import com.gym.membership.domain.repository.TrainerRepository;
import com.gym.membership.domain.repository.TrainingSessionRepository;
import com.gym.membership.dto.MemberRequestDTO;
import com.gym.membership.dto.MemberResponseDTO;
import com.gym.membership.mapper.MemberMapper;

@ExtendWith(MockitoExtension.class)
public class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TrainerRepository trainerRepository;                   // FK

    @Mock
    private MemberInfoRepository memberInfoRepository;             // ใช้เช็กก่อนลบ

    @Mock
    private MembershipRepository membershipRepository;             // ใช้เช็กก่อนลบ

    @Mock
    private TrainingSessionRepository trainingSessionRepository;   // ใช้เช็กก่อนลบ

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberServiceImpl memberService;

    // ---------- ข้อมูลตัวอย่าง ----------
    private Trainer sampleTrainer() {
        return new Trainer(1L, "โค้ชเอ", "0812345678", "เวท");
    }

    private Member sampleMember(Long id) {
        Member member = new Member();
        member.setMemberId(id);
        member.setUsername("testuser");
        member.setPassword("secret123");
        return member;
    }

    private MemberRequestDTO sampleRequest(Long trainerId) {
        MemberRequestDTO dto = new MemberRequestDTO();
        dto.setUsername("somchai");
        dto.setEmail("somchai@example.com");
        dto.setPhone("0812345678");
        dto.setPassword("newsecret123");
        dto.setTrainerId(trainerId);
        return dto;
    }

    private MemberResponseDTO sampleResponse(Long id) {
        MemberResponseDTO response = new MemberResponseDTO();
        response.setMemberId(id);
        response.setUsername("somchai");
        response.setEmail("somchai@example.com");
        response.setPhone("0812345678");
        return response;
    }

    private MemberInfo infoOf(Member member) {
        MemberInfo info = new MemberInfo();
        info.setInfoId(1L);
        info.setMember(member);
        return info;
    }

    // ① สร้าง Member พร้อม trainer → ผูก trainer ถูกคน
    @Test
    void createMember_withTrainer_linksTrainer() {
        // Given
        Trainer trainer = sampleTrainer();
        MemberRequestDTO dto = sampleRequest(1L);
        Member member = new Member();

        when(memberMapper.toEntity(dto)).thenReturn(member);
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));
        when(memberRepository.save(member)).thenReturn(member);
        when(memberMapper.toResponse(member)).thenReturn(sampleResponse(1L));

        // When
        MemberResponseDTO result = memberService.createMember(dto);

        // Then
        assertEquals(trainer, member.getTrainer());
        assertEquals(1L, result.getMemberId());
        verify(memberRepository).save(member);
    }

    // ② สร้าง Member โดยไม่ใส่ trainerId → ไม่ไปค้น trainer และ trainer เป็น null
    @Test
    void createMember_withoutTrainer_skipsTrainerLookup() {
        // Given
        MemberRequestDTO dto = sampleRequest(null);
        Member member = new Member();

        when(memberMapper.toEntity(dto)).thenReturn(member);
        when(memberRepository.save(member)).thenReturn(member);
        when(memberMapper.toResponse(member)).thenReturn(sampleResponse(1L));

        // When
        memberService.createMember(dto);

        // Then
        assertNull(member.getTrainer());
        verifyNoInteractions(trainerRepository);
        verify(memberRepository).save(member);
    }

    // ③ สร้าง Member โดย trainer ไม่มี → 404 และห้าม save
    @Test
    void createMember_trainerNotFound_throws404() {
        // Given
        MemberRequestDTO dto = sampleRequest(99L);
        when(memberMapper.toEntity(dto)).thenReturn(new Member());
        when(trainerRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> memberService.createMember(dto));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        verify(memberRepository, never()).save(any());
    }

    // ④ หา Member ที่มีอยู่ → ได้ข้อมูล
    @Test
    void getMemberById_found_returnsResponse() {
        // Given
        Member member = sampleMember(1L);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberMapper.toResponse(member)).thenReturn(sampleResponse(1L));

        // When
        MemberResponseDTO result = memberService.getMemberById(1L);

        // Then
        assertEquals(1L, result.getMemberId());
        assertEquals("somchai", result.getUsername());
    }

    // ⑤ หา Member ที่ไม่มี → 404
    @Test
    void getMemberById_notFound_throws404() {
        // Given
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> memberService.getMemberById(99L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    // ⑥ ดู Member ทั้งหมด → จำนวนต้องครบ
    @Test
    void getAllMembers_returnsAll() {
        // Given
        Member m1 = sampleMember(1L);
        Member m2 = sampleMember(2L);
        when(memberRepository.findAll()).thenReturn(List.of(m1, m2));
        when(memberMapper.toResponse(m1)).thenReturn(sampleResponse(1L));
        when(memberMapper.toResponse(m2)).thenReturn(sampleResponse(2L));

        // When
        List<MemberResponseDTO> result = memberService.getAllMembers();

        // Then
        assertEquals(2, result.size());
        assertEquals(2L, result.get(1).getMemberId());
    }

    // ⑦ แก้ไข Member → field ใหม่และ trainer ใหม่ต้องถูก set ก่อน save
    @Test
    void updateMember_changesFieldsAndTrainer() {
        // Given
        Member member = sampleMember(1L);
        Trainer trainer = sampleTrainer();
        MemberRequestDTO dto = sampleRequest(1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));
        when(memberRepository.save(member)).thenReturn(member);
        when(memberMapper.toResponse(member)).thenReturn(sampleResponse(1L));

        // When
        memberService.updateMember(1L, dto);

        // Then
        assertEquals("somchai", member.getUsername());
        assertEquals("somchai@example.com", member.getEmail());
        assertEquals("0812345678", member.getPhone());
        assertEquals("newsecret123", member.getPassword());
        assertEquals(trainer, member.getTrainer());
        verify(memberRepository).save(member);
    }

    // ⑧ แก้ไขโดยไม่ส่ง trainerId → trainer ถูกล้างเป็น null
    @Test
    void updateMember_withoutTrainerId_clearsTrainer() {
        // Given
        Member member = sampleMember(1L);
        member.setTrainer(sampleTrainer());
        MemberRequestDTO dto = sampleRequest(null);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.save(member)).thenReturn(member);
        when(memberMapper.toResponse(member)).thenReturn(sampleResponse(1L));

        // When
        memberService.updateMember(1L, dto);

        // Then
        assertNull(member.getTrainer());
        verifyNoInteractions(trainerRepository);
    }

    // ⑨ แก้ไข Member ที่ไม่มี → 404 และห้าม save
    @Test
    void updateMember_memberNotFound_throws404() {
        // Given
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> memberService.updateMember(99L, sampleRequest(1L)));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        verify(memberRepository, never()).save(any());
    }

    // ⑩ แก้ไขโดย trainer ไม่มี → 404 และห้าม save
    @Test
    void updateMember_trainerNotFound_throws404() {
        // Given
        when(memberRepository.findById(1L)).thenReturn(Optional.of(sampleMember(1L)));
        when(trainerRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> memberService.updateMember(1L, sampleRequest(99L)));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        verify(memberRepository, never()).save(any());
    }

    // ⑪ ลบ Member ที่ไม่มี MemberInfo → ต้องสั่งลบจริง
    @Test
    void deleteMember_noMemberInfo_callsDelete() {
        // Given
        Member member = sampleMember(1L);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberInfoRepository.findAll()).thenReturn(List.of());

        // When
        memberService.deleteMember(1L);

        // Then
        verify(memberRepository).delete(member);
    }

    // ⑫ ลบ Member ที่ยังมี MemberInfo ผูกอยู่ → 409 และห้ามลบ
    @Test
    void deleteMember_stillHasMemberInfo_throws409() {
        // Given
        Member member = sampleMember(1L);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberInfoRepository.findAll()).thenReturn(List.of(infoOf(member)));

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> memberService.deleteMember(1L));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(memberRepository, never()).delete(any());
    }

    // ⑬ MemberInfo ของคนอื่นไม่เกี่ยว → ยังลบ Member นี้ได้
    @Test
    void deleteMember_otherMembersInfoExists_stillDeletes() {
        // Given
        Member member = sampleMember(1L);
        Member other = sampleMember(2L);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberInfoRepository.findAll()).thenReturn(List.of(infoOf(other)));

        // When
        memberService.deleteMember(1L);

        // Then
        verify(memberRepository).delete(member);
    }

    // ⑭ ลบ Member ที่ไม่มี → 404 และห้ามสั่งลบ
    @Test
    void deleteMember_notFound_throws404() {
        // Given
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> memberService.deleteMember(99L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        verify(memberRepository, never()).delete(any());
    }

    // ⑮ ลบ Member ที่ยังมีการสมัครแพ็กเกจ → 409 และห้ามลบ
    @Test
    void deleteMember_stillHasMembership_throws409() {
        // Given
        Member member = sampleMember(1L);
        Membership membership = new Membership();
        membership.setMember(member);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberInfoRepository.findAll()).thenReturn(List.of());
        when(membershipRepository.findAll()).thenReturn(List.of(membership));

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> memberService.deleteMember(1L));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(memberRepository, never()).delete(any());
    }

    // ⑯ ลบ Member ที่ยังมีนัดฝึก → 409 และห้ามลบ
    @Test
    void deleteMember_stillHasTrainingSession_throws409() {
        // Given
        Member member = sampleMember(1L);
        TrainingSession session = new TrainingSession();
        session.setMemberId(member);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberInfoRepository.findAll()).thenReturn(List.of());
        when(membershipRepository.findAll()).thenReturn(List.of());
        when(trainingSessionRepository.findAll()).thenReturn(List.of(session));

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> memberService.deleteMember(1L));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(memberRepository, never()).delete(any());
    }
}
