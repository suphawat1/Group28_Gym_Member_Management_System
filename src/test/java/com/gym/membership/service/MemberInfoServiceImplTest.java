package com.gym.membership.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.gym.membership.domain.entity.MemberInfo;
import com.gym.membership.domain.repository.MemberInfoRepository;
import com.gym.membership.domain.repository.MemberRepository;
import com.gym.membership.dto.MemberInfoRequestDTO;
import com.gym.membership.dto.MemberInfoResponseDTO;
import com.gym.membership.mapper.MemberInfoMapper;

@ExtendWith(MockitoExtension.class)
public class MemberInfoServiceImplTest {

    @Mock
    private MemberInfoRepository memberInfoRepository;

    @Mock
    private MemberRepository memberRepository;                     // FK

    @Mock
    private MemberInfoMapper memberInfoMapper;

    @InjectMocks
    private MemberInfoServiceImpl memberInfoService;

    // ---------- ข้อมูลตัวอย่าง ----------
    private Member sampleMember(Long id) {
        Member member = new Member();
        member.setMemberId(id);
        member.setUsername("testuser");
        return member;
    }

    private MemberInfo sampleInfo(Long infoId, Member member) {
        MemberInfo info = new MemberInfo();
        info.setInfoId(infoId);
        info.setMember(member);
        info.setFullName("สมชาย ใจดี");
        return info;
    }

    private MemberInfoRequestDTO sampleRequest(Long memberId) {
        MemberInfoRequestDTO dto = new MemberInfoRequestDTO();
        dto.setMemberId(memberId);
        dto.setFullName("สมชาย ใจดี");
        dto.setGender("ชาย");
        dto.setDateOfBirth(LocalDate.of(2000, 1, 15));
        dto.setEmergencyContact("0899999999");
        return dto;
    }

    private MemberInfoResponseDTO sampleResponse(Long infoId) {
        MemberInfoResponseDTO response = new MemberInfoResponseDTO();
        response.setInfoId(infoId);
        response.setMemberId(1L);
        response.setFullName("สมชาย ใจดี");
        return response;
    }

    // ① สร้าง MemberInfo → ผูก member ถูกคน
    @Test
    void createMemberInfo_linksMember() {
        // Given
        Member member = sampleMember(1L);
        MemberInfoRequestDTO dto = sampleRequest(1L);
        MemberInfo info = new MemberInfo();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberInfoRepository.findAll()).thenReturn(List.of());
        when(memberInfoMapper.toEntity(dto)).thenReturn(info);
        when(memberInfoRepository.save(info)).thenReturn(info);
        when(memberInfoMapper.toResponse(info)).thenReturn(sampleResponse(1L));

        // When
        MemberInfoResponseDTO result = memberInfoService.createMemberInfo(dto);

        // Then
        assertEquals(member, info.getMember());
        assertEquals(1L, result.getInfoId());
        verify(memberInfoRepository).save(info);
    }

    // ② สร้างโดย member ไม่มี → 404 และห้าม save
    @Test
    void createMemberInfo_memberNotFound_throws404() {
        // Given
        MemberInfoRequestDTO dto = sampleRequest(99L);
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> memberInfoService.createMemberInfo(dto));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        verify(memberInfoRepository, never()).save(any());
    }

    // ③ Member นี้มี MemberInfo อยู่แล้ว (One-to-One) → 409 และห้าม save
    @Test
    void createMemberInfo_memberAlreadyHasInfo_throws409() {
        // Given
        Member member = sampleMember(1L);
        MemberInfoRequestDTO dto = sampleRequest(1L);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberInfoRepository.findAll()).thenReturn(List.of(sampleInfo(5L, member)));

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> memberInfoService.createMemberInfo(dto));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(memberInfoRepository, never()).save(any());
    }

    // ④ หา MemberInfo ที่มีอยู่ → ได้ข้อมูล
    @Test
    void getMemberInfoById_found_returnsResponse() {
        // Given
        MemberInfo info = sampleInfo(1L, sampleMember(1L));
        when(memberInfoRepository.findById(1L)).thenReturn(Optional.of(info));
        when(memberInfoMapper.toResponse(info)).thenReturn(sampleResponse(1L));

        // When
        MemberInfoResponseDTO result = memberInfoService.getMemberInfoById(1L);

        // Then
        assertEquals(1L, result.getInfoId());
        assertEquals("สมชาย ใจดี", result.getFullName());
    }

    // ⑤ หา MemberInfo ที่ไม่มี → 404
    @Test
    void getMemberInfoById_notFound_throws404() {
        // Given
        when(memberInfoRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> memberInfoService.getMemberInfoById(99L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    // ⑥ ดู MemberInfo ทั้งหมด → จำนวนต้องครบ
    @Test
    void getAllMemberInfos_returnsAll() {
        // Given
        MemberInfo i1 = sampleInfo(1L, sampleMember(1L));
        MemberInfo i2 = sampleInfo(2L, sampleMember(2L));
        when(memberInfoRepository.findAll()).thenReturn(List.of(i1, i2));
        when(memberInfoMapper.toResponse(i1)).thenReturn(sampleResponse(1L));
        when(memberInfoMapper.toResponse(i2)).thenReturn(sampleResponse(2L));

        // When
        List<MemberInfoResponseDTO> result = memberInfoService.getAllMemberInfos();

        // Then
        assertEquals(2, result.size());
        assertEquals(2L, result.get(1).getInfoId());
    }

    // ⑦ แก้ไข MemberInfo (ของ member เดิมตัวเอง) → field ใหม่ต้องถูก set ก่อน save
    @Test
    void updateMemberInfo_changesFields() {
        // Given
        Member member = sampleMember(1L);
        MemberInfo info = sampleInfo(1L, member);
        MemberInfoRequestDTO dto = sampleRequest(1L);
        dto.setFullName("สมหญิง ใจงาม");
        dto.setGender("หญิง");
        dto.setEmergencyContact("0811111111");

        when(memberInfoRepository.findById(1L)).thenReturn(Optional.of(info));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberInfoRepository.findAll()).thenReturn(List.of(info));   // มีแค่ตัวเอง ต้องผ่าน
        when(memberInfoRepository.save(info)).thenReturn(info);
        when(memberInfoMapper.toResponse(info)).thenReturn(sampleResponse(1L));

        // When
        memberInfoService.updateMemberInfo(1L, dto);

        // Then
        assertEquals("สมหญิง ใจงาม", info.getFullName());
        assertEquals("หญิง", info.getGender());
        assertEquals(LocalDate.of(2000, 1, 15), info.getDateOfBirth());
        assertEquals("0811111111", info.getEmergencyContact());
        assertEquals(member, info.getMember());
        verify(memberInfoRepository).save(info);
    }

    // ⑧ แก้ไขให้ไปผูกกับ member ที่มี MemberInfo อื่นอยู่แล้ว → 409 และห้าม save
    @Test
    void updateMemberInfo_memberTakenByAnotherInfo_throws409() {
        // Given
        Member member1 = sampleMember(1L);
        Member member2 = sampleMember(2L);
        MemberInfo myInfo = sampleInfo(1L, member1);
        MemberInfo otherInfo = sampleInfo(2L, member2);
        MemberInfoRequestDTO dto = sampleRequest(2L);                     // พยายามย้ายไปผูกกับ member 2

        when(memberInfoRepository.findById(1L)).thenReturn(Optional.of(myInfo));
        when(memberRepository.findById(2L)).thenReturn(Optional.of(member2));
        when(memberInfoRepository.findAll()).thenReturn(List.of(myInfo, otherInfo));

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> memberInfoService.updateMemberInfo(1L, dto));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(memberInfoRepository, never()).save(any());
    }

    // ⑨ แก้ไข MemberInfo ที่ไม่มี → 404 และห้าม save
    @Test
    void updateMemberInfo_infoNotFound_throws404() {
        // Given
        when(memberInfoRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> memberInfoService.updateMemberInfo(99L, sampleRequest(1L)));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        verify(memberInfoRepository, never()).save(any());
    }

    // ⑩ แก้ไขโดย member ไม่มี → 404 และห้าม save
    @Test
    void updateMemberInfo_memberNotFound_throws404() {
        // Given
        MemberInfo info = sampleInfo(1L, sampleMember(1L));
        when(memberInfoRepository.findById(1L)).thenReturn(Optional.of(info));
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> memberInfoService.updateMemberInfo(1L, sampleRequest(99L)));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        verify(memberInfoRepository, never()).save(any());
    }

    // ⑪ ลบ MemberInfo ที่มีอยู่ → ต้องสั่งลบจริง
    @Test
    void deleteMemberInfo_found_callsDelete() {
        // Given
        MemberInfo info = sampleInfo(1L, sampleMember(1L));
        when(memberInfoRepository.findById(1L)).thenReturn(Optional.of(info));

        // When
        memberInfoService.deleteMemberInfo(1L);

        // Then
        verify(memberInfoRepository).delete(info);
    }

    // ⑫ ลบ MemberInfo ที่ไม่มี → 404 และห้ามสั่งลบ
    @Test
    void deleteMemberInfo_notFound_throws404() {
        // Given
        when(memberInfoRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> memberInfoService.deleteMemberInfo(99L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        verify(memberInfoRepository, never()).delete(any());
    }
}