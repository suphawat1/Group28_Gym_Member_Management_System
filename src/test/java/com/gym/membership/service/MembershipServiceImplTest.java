package com.gym.membership.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import com.gym.membership.domain.entity.Membership;
import com.gym.membership.domain.entity.MembershipPlan;
import com.gym.membership.domain.repository.MemberRepository;
import com.gym.membership.domain.repository.MembershipPlanRepository;
import com.gym.membership.domain.repository.MembershipRepository;
import com.gym.membership.dto.MembershipRequestDTO;
import com.gym.membership.dto.MembershipResponseDTO;
import com.gym.membership.mapper.MembershipMapper;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceImplTest {

    @Mock
    private MembershipRepository membershipRepository;           // ลูกน้องปลอม 1

    @Mock
    private MemberRepository memberRepository;                   // ลูกน้องปลอม 2 (FK)

    @Mock
    private MembershipPlanRepository membershipPlanRepository;   // ลูกน้องปลอม 3 (FK)

    @Mock
    private MembershipMapper membershipMapper;                   // ลูกน้องปลอม 4

    @InjectMocks
    private MembershipServiceImpl membershipService;             // หัวหน้าตัวจริงที่ถูกสอบ

    // ---------- ข้อมูลตัวอย่าง ใช้ซ้ำหลายข้อ ----------
    private Member sampleMember() {
        Member member = new Member();
        member.setMemberId(1L);
        member.setUsername("testuser");
        return member;
    }

    private MembershipPlan samplePlan() {
        // แพ็กเกจรายเดือน 30 วัน
        return new MembershipPlan(1L, "รายเดือน", new BigDecimal("1500.00"), 30);
    }

    private MembershipRequestDTO sampleRequest(Long memberId, Long planId, LocalDate startDate) {
        return new MembershipRequestDTO(memberId, planId, startDate);
    }

    private MembershipResponseDTO sampleResponse(Long membershipId) {
        return new MembershipResponseDTO(membershipId, 1L, 1L,
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31));
    }

    // ① สมัครสมาชิก → endDate ต้องเท่ากับ startDate + จำนวนวันของแพ็กเกจ และผูก member/plan ถูกตัว
    @Test
    void createMembership_calculatesEndDateAndLinksMemberPlan() {
        // Given
        Member member = sampleMember();
        MembershipPlan plan = samplePlan();
        MembershipRequestDTO dto = sampleRequest(1L, 1L, LocalDate.of(2026, 10, 1));
        Membership membership = new Membership();
        membership.setStartDate(LocalDate.of(2026, 10, 1));

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(membershipPlanRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(membershipMapper.toEntity(dto)).thenReturn(membership);
        when(membershipRepository.save(membership)).thenReturn(membership);
        when(membershipMapper.toResponse(membership)).thenReturn(sampleResponse(1L));

        // When
        MembershipResponseDTO result = membershipService.createMembership(dto);

        // Then
        assertEquals(LocalDate.of(2026, 10, 31), membership.getEndDate());   // 1 ต.ค. + 30 วัน
        assertEquals(member, membership.getMember());
        assertEquals(plan, membership.getMembershipPlan());
        assertEquals(1L, result.getMembershipId());
        verify(membershipRepository).save(membership);
    }

    // ② สมัครด้วยสมาชิกที่ไม่มี → ต้องโยน 404 และห้าม save
    @Test
    void createMembership_memberNotFound_throws404() {
        // Given
        MembershipRequestDTO dto = sampleRequest(99L, 1L, LocalDate.of(2026, 10, 1));
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> membershipService.createMembership(dto));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        verify(membershipRepository, never()).save(any());
    }

    // ③ สมัครด้วยแพ็กเกจที่ไม่มี → ต้องโยน 404 และห้าม save
    @Test
    void createMembership_planNotFound_throws404() {
        // Given
        MembershipRequestDTO dto = sampleRequest(1L, 99L, LocalDate.of(2026, 10, 1));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(sampleMember()));
        when(membershipPlanRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> membershipService.createMembership(dto));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        verify(membershipRepository, never()).save(any());
    }

    // ④ หาการสมัครที่มีอยู่ → ได้ข้อมูลถูก
    @Test
    void getMembershipById_found_returnsResponse() {
        // Given
        Membership membership = new Membership();
        membership.setMembershipId(1L);
        when(membershipRepository.findById(1L)).thenReturn(Optional.of(membership));
        when(membershipMapper.toResponse(membership)).thenReturn(sampleResponse(1L));

        // When
        MembershipResponseDTO result = membershipService.getMembershipById(1L);

        // Then
        assertEquals(1L, result.getMembershipId());
        assertEquals(LocalDate.of(2026, 10, 31), result.getEndDate());
    }

    // ⑤ หาการสมัครที่ไม่มี → ต้องโยน 404
    @Test
    void getMembershipById_notFound_throws404() {
        // Given
        when(membershipRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> membershipService.getMembershipById(99L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    // ⑥ ดูทั้งหมด → จำนวนต้องครบ
    @Test
    void getAllMemberships_returnsAll() {
        // Given
        Membership m1 = new Membership();
        Membership m2 = new Membership();
        when(membershipRepository.findAll()).thenReturn(List.of(m1, m2));
        when(membershipMapper.toResponse(m1)).thenReturn(sampleResponse(1L));
        when(membershipMapper.toResponse(m2)).thenReturn(sampleResponse(2L));

        // When
        List<MembershipResponseDTO> result = membershipService.getAllMemberships();

        // Then
        assertEquals(2, result.size());
        assertEquals(2L, result.get(1).getMembershipId());
    }

    // ⑦ แก้ไขวันเริ่ม → endDate ต้องถูกคำนวณใหม่ตามวันเริ่มใหม่
    @Test
    void updateMembership_recalculatesEndDate() {
        // Given
        Membership membership = new Membership();
        membership.setMembershipId(1L);
        membership.setStartDate(LocalDate.of(2026, 10, 1));
        membership.setEndDate(LocalDate.of(2026, 10, 31));

        MembershipRequestDTO dto = sampleRequest(1L, 1L, LocalDate.of(2026, 11, 1));

        when(membershipRepository.findById(1L)).thenReturn(Optional.of(membership));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(sampleMember()));
        when(membershipPlanRepository.findById(1L)).thenReturn(Optional.of(samplePlan()));
        when(membershipRepository.save(membership)).thenReturn(membership);
        when(membershipMapper.toResponse(membership)).thenReturn(sampleResponse(1L));

        // When
        membershipService.updateMembership(1L, dto);

        // Then
        assertEquals(LocalDate.of(2026, 11, 1), membership.getStartDate());
        assertEquals(LocalDate.of(2026, 12, 1), membership.getEndDate());   // 1 พ.ย. + 30 วัน
        verify(membershipRepository).save(membership);
    }

    // ⑧ ลบที่มีอยู่ → ต้องสั่งลบจริง
    @Test
    void deleteMembership_found_callsDelete() {
        // Given
        Membership membership = new Membership();
        when(membershipRepository.findById(1L)).thenReturn(Optional.of(membership));

        // When
        membershipService.deleteMembership(1L);

        // Then
        verify(membershipRepository).delete(membership);
    }

    // ⑨ ลบที่ไม่มี → ต้องโยน 404 และห้ามสั่งลบ
    @Test
    void deleteMembership_notFound_throws404() {
        // Given
        when(membershipRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        assertThrows(ResponseStatusException.class,
                () -> membershipService.deleteMembership(99L));
        verify(membershipRepository, never()).delete(any());
    }
}
