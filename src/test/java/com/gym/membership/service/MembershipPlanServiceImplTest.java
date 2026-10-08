package com.gym.membership.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.gym.membership.domain.entity.MembershipPlan;
import com.gym.membership.domain.repository.MembershipPlanRepository;
import com.gym.membership.dto.MembershipPlanRequestDTO;
import com.gym.membership.dto.MembershipPlanResponseDTO;
import com.gym.membership.mapper.MembershipPlanMapper;

@ExtendWith(MockitoExtension.class)
public class MembershipPlanServiceImplTest {

    @Mock
    private MembershipPlanRepository membershipPlanRepository;   // ลูกน้องปลอม 1

    @Mock
    private MembershipPlanMapper membershipPlanMapper;           // ลูกน้องปลอม 2

    @InjectMocks
    private MembershipPlanServiceImpl membershipPlanService;     // หัวหน้าตัวจริงที่ถูกสอบ

    // ① สร้างแพ็กเกจ → ต้องได้ซองขาออกที่มี id กลับมา
    @Test
    void createMembershipPlan_returnsResponse() {
        // Given
        MembershipPlanRequestDTO dto = new MembershipPlanRequestDTO("รายเดือน", new BigDecimal("1500.00"), 30);
        MembershipPlan plan = new MembershipPlan(null, "รายเดือน", new BigDecimal("1500.00"), 30);
        MembershipPlan savedPlan = new MembershipPlan(1L, "รายเดือน", new BigDecimal("1500.00"), 30);
        MembershipPlanResponseDTO response = new MembershipPlanResponseDTO(1L, "รายเดือน", new BigDecimal("1500.00"), 30);

        when(membershipPlanMapper.toEntity(dto)).thenReturn(plan);
        when(membershipPlanRepository.save(plan)).thenReturn(savedPlan);
        when(membershipPlanMapper.toResponse(savedPlan)).thenReturn(response);

        // When
        MembershipPlanResponseDTO result = membershipPlanService.createMembershipPlan(dto);

        // Then
        assertEquals(1L, result.getPlanId());
        assertEquals("รายเดือน", result.getPlanName());
        assertEquals(new BigDecimal("1500.00"), result.getPrice());
        verify(membershipPlanRepository).save(plan);
    }

    // ② หาแพ็กเกจที่มีอยู่ → ได้ข้อมูลแพ็กเกจนั้น
    @Test
    void getMembershipPlanById_found_returnsResponse() {
        // Given
        MembershipPlan plan = new MembershipPlan(1L, "รายเดือน", new BigDecimal("1500.00"), 30);
        MembershipPlanResponseDTO response = new MembershipPlanResponseDTO(1L, "รายเดือน", new BigDecimal("1500.00"), 30);

        when(membershipPlanRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(membershipPlanMapper.toResponse(plan)).thenReturn(response);

        // When
        MembershipPlanResponseDTO result = membershipPlanService.getMembershipPlanById(1L);

        // Then
        assertEquals(1L, result.getPlanId());
        assertEquals(30, result.getDurationDays());
    }

    // ③ หาแพ็กเกจที่ไม่มี → ต้องโยน 404
    @Test
    void getMembershipPlanById_notFound_throws404() {
        // Given
        when(membershipPlanRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> membershipPlanService.getMembershipPlanById(99L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    // ④ ดูทั้งหมด → จำนวนต้องครบ
    @Test
    void getAllMembershipPlans_returnsAll() {
        // Given
        MembershipPlan p1 = new MembershipPlan(1L, "รายเดือน", new BigDecimal("1500.00"), 30);
        MembershipPlan p2 = new MembershipPlan(2L, "รายปี", new BigDecimal("12000.00"), 365);

        when(membershipPlanRepository.findAll()).thenReturn(List.of(p1, p2));
        when(membershipPlanMapper.toResponse(p1))
                .thenReturn(new MembershipPlanResponseDTO(1L, "รายเดือน", new BigDecimal("1500.00"), 30));
        when(membershipPlanMapper.toResponse(p2))
                .thenReturn(new MembershipPlanResponseDTO(2L, "รายปี", new BigDecimal("12000.00"), 365));

        // When
        List<MembershipPlanResponseDTO> result = membershipPlanService.getAllMembershipPlans();

        // Then
        assertEquals(2, result.size());
        assertEquals("รายปี", result.get(1).getPlanName());
    }

    // ⑤ แก้ไข → ราคาและจำนวนวันต้องเปลี่ยนเป็นค่าใหม่ก่อน save
    @Test
    void updateMembershipPlan_changesFields() {
        // Given
        MembershipPlan plan = new MembershipPlan(1L, "รายเดือน", new BigDecimal("1500.00"), 30);
        MembershipPlanRequestDTO dto = new MembershipPlanRequestDTO("รายเดือนพิเศษ", new BigDecimal("1299.50"), 45);
        MembershipPlanResponseDTO response = new MembershipPlanResponseDTO(1L, "รายเดือนพิเศษ", new BigDecimal("1299.50"), 45);

        when(membershipPlanRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(membershipPlanRepository.save(plan)).thenReturn(plan);
        when(membershipPlanMapper.toResponse(plan)).thenReturn(response);

        // When
        membershipPlanService.updateMembershipPlan(1L, dto);

        // Then
        assertEquals("รายเดือนพิเศษ", plan.getPlanName());
        assertEquals(new BigDecimal("1299.50"), plan.getPrice());
        assertEquals(45, plan.getDurationDays());
        verify(membershipPlanRepository).save(plan);
    }

    // ⑥ ลบแพ็กเกจที่มีอยู่ → ต้องสั่งลบจริง
    @Test
    void deleteMembershipPlan_found_callsDelete() {
        // Given
        MembershipPlan plan = new MembershipPlan(1L, "รายเดือน", new BigDecimal("1500.00"), 30);
        when(membershipPlanRepository.findById(1L)).thenReturn(Optional.of(plan));

        // When
        membershipPlanService.deleteMembershipPlan(1L);

        // Then
        verify(membershipPlanRepository).delete(plan);
    }

    // ⑦ ลบแพ็กเกจที่ไม่มี → ต้องโยน 404 และห้ามสั่งลบ
    @Test
    void deleteMembershipPlan_notFound_throws404() {
        // Given
        when(membershipPlanRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        assertThrows(ResponseStatusException.class,
                () -> membershipPlanService.deleteMembershipPlan(99L));
        verify(membershipPlanRepository, never()).delete(any());
    }
}