package com.gym.membership.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
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
import com.gym.membership.domain.entity.Trainer;
import com.gym.membership.domain.entity.TrainingSession;
import com.gym.membership.domain.repository.MemberRepository;
import com.gym.membership.domain.repository.TrainerRepository;
import com.gym.membership.domain.repository.TrainingSessionRepository;
import com.gym.membership.dto.TrainingSessionRequestDTO;
import com.gym.membership.dto.TrainingSessionResponseDTO;
import com.gym.membership.mapper.TrainingSessionMapper;

@ExtendWith(MockitoExtension.class)
public class TrainingSessionServiceImplTest {

    @Mock
    private TrainingSessionRepository trainingSessionRepository;   // ลูกน้องปลอม 1

    @Mock
    private TrainerRepository trainerRepository;                   // ลูกน้องปลอม 2 (FK)

    @Mock
    private MemberRepository memberRepository;                     // ลูกน้องปลอม 3 (FK)

    @Mock
    private TrainingSessionMapper trainingSessionMapper;           // ลูกน้องปลอม 4

    @InjectMocks
    private TrainingSessionServiceImpl trainingSessionService;     // หัวหน้าตัวจริงที่ถูกสอบ

    // ---------- ข้อมูลตัวอย่าง ใช้ซ้ำหลายข้อ ----------
    private Trainer sampleTrainer() {
        return new Trainer(1L, "โค้ชเอ", "0812345678", "เวท");
    }

    private Member sampleMember() {
        Member member = new Member();
        member.setMemberId(1L);
        member.setUsername("testuser");
        return member;
    }

    private TrainingSessionRequestDTO sampleRequest(Long trainerId, Long memberId) {
        TrainingSessionRequestDTO dto = new TrainingSessionRequestDTO();
        dto.setTrainerId(trainerId);
        dto.setMemberId(memberId);
        dto.setSessionDate(LocalDate.of(2026, 10, 10));
        dto.setSessionTime(LocalTime.of(18, 30));
        return dto;
    }

    private TrainingSessionResponseDTO sampleResponse(Long sessionId) {
        TrainingSessionResponseDTO response = new TrainingSessionResponseDTO();
        response.setSessionId(sessionId);
        response.setTrainerId(1L);
        response.setMemberId(1L);
        response.setSessionDate(LocalDate.of(2026, 10, 10));
        response.setSessionTime(LocalTime.of(18, 30));
        response.setStatus("BOOKED");
        return response;
    }

    // ① จองนัด → status ต้องเป็น BOOKED และผูก trainer/member ถูกคน
    @Test
    void createTrainingSession_setsStatusBookedAndLinksTrainerMember() {
        // Given
        Trainer trainer = sampleTrainer();
        Member member = sampleMember();
        TrainingSessionRequestDTO dto = sampleRequest(1L, 1L);
        TrainingSession session = new TrainingSession();

        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(trainingSessionMapper.toEntity(dto)).thenReturn(session);
        when(trainingSessionRepository.save(session)).thenReturn(session);
        when(trainingSessionMapper.toResponse(session)).thenReturn(sampleResponse(1L));

        // When
        TrainingSessionResponseDTO result = trainingSessionService.createTrainingSession(dto);

        // Then
        assertEquals("BOOKED", session.getStatus());
        assertEquals(trainer, session.getTrainerId());
        assertEquals(member, session.getMemberId());
        assertEquals(1L, result.getSessionId());
        verify(trainingSessionRepository).save(session);
    }

    // ② จองนัดกับเทรนเนอร์ที่ไม่มี → ต้องโยน 404 และห้าม save
    @Test
    void createTrainingSession_trainerNotFound_throws404() {
        // Given
        TrainingSessionRequestDTO dto = sampleRequest(99L, 1L);
        when(trainerRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> trainingSessionService.createTrainingSession(dto));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        verify(trainingSessionRepository, never()).save(any());
    }

    // ③ หานัดที่มีอยู่ → ได้ข้อมูลนัดนั้น
    @Test
    void getTrainingSessionById_found_returnsResponse() {
        // Given
        TrainingSession session = new TrainingSession();
        session.setSessionId(1L);
        when(trainingSessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(trainingSessionMapper.toResponse(session)).thenReturn(sampleResponse(1L));

        // When
        TrainingSessionResponseDTO result = trainingSessionService.getTrainingSessionById(1L);

        // Then
        assertEquals(1L, result.getSessionId());
        assertEquals("BOOKED", result.getStatus());
    }

    // ④ หานัดที่ไม่มี → ต้องโยน 404
    @Test
    void getTrainingSessionById_notFound_throws404() {
        // Given
        when(trainingSessionRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> trainingSessionService.getTrainingSessionById(99L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    // ⑤ ดูนัดทั้งหมด → จำนวนต้องครบ
    @Test
    void getAllTrainingSessions_returnsAll() {
        // Given
        TrainingSession s1 = new TrainingSession();
        TrainingSession s2 = new TrainingSession();
        when(trainingSessionRepository.findAll()).thenReturn(List.of(s1, s2));
        when(trainingSessionMapper.toResponse(s1)).thenReturn(sampleResponse(1L));
        when(trainingSessionMapper.toResponse(s2)).thenReturn(sampleResponse(2L));

        // When
        List<TrainingSessionResponseDTO> result = trainingSessionService.getAllTrainingSessions();

        // Then
        assertEquals(2, result.size());
        assertEquals(2L, result.get(1).getSessionId());
    }

    // ⑥ แก้ไขนัด → วันและเวลาต้องเปลี่ยนเป็นค่าใหม่ก่อน save
    @Test
    void updateTrainingSession_changesDateAndTime() {
        // Given
        TrainingSession session = new TrainingSession();
        session.setSessionId(1L);
        session.setSessionDate(LocalDate.of(2026, 10, 10));
        session.setSessionTime(LocalTime.of(18, 30));

        TrainingSessionRequestDTO dto = sampleRequest(1L, 1L);
        dto.setSessionDate(LocalDate.of(2026, 10, 12));
        dto.setSessionTime(LocalTime.of(9, 0));

        when(trainingSessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(sampleTrainer()));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(sampleMember()));
        when(trainingSessionRepository.save(session)).thenReturn(session);
        when(trainingSessionMapper.toResponse(session)).thenReturn(sampleResponse(1L));

        // When
        trainingSessionService.updateTrainingSession(1L, dto);

        // Then
        assertEquals(LocalDate.of(2026, 10, 12), session.getSessionDate());
        assertEquals(LocalTime.of(9, 0), session.getSessionTime());
        verify(trainingSessionRepository).save(session);
    }

    // ⑦ ลบนัดที่มีอยู่ → ต้องสั่งลบจริง
    @Test
    void deleteTrainingSession_found_callsDelete() {
        // Given
        TrainingSession session = new TrainingSession();
        when(trainingSessionRepository.findById(1L)).thenReturn(Optional.of(session));

        // When
        trainingSessionService.deleteTrainingSession(1L);

        // Then
        verify(trainingSessionRepository).delete(session);
    }

    // ⑧ ลบนัดที่ไม่มี → ต้องโยน 404 และห้ามสั่งลบ
    @Test
    void deleteTrainingSession_notFound_throws404() {
        // Given
        when(trainingSessionRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        assertThrows(ResponseStatusException.class,
                () -> trainingSessionService.deleteTrainingSession(99L));
        verify(trainingSessionRepository, never()).delete(any());
    }
}