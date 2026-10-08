package com.gym.membership.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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

import com.gym.membership.domain.entity.Trainer;
import com.gym.membership.domain.repository.TrainerRepository;
import com.gym.membership.dto.TrainerRequestDTO;
import com.gym.membership.dto.TrainerResponseDTO;
import com.gym.membership.mapper.TrainerMapper;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;   // ลูกน้องปลอม 1

    @Mock
    private TrainerMapper trainerMapper;           // ลูกน้องปลอม 2

    @InjectMocks
    private TrainerServiceImpl trainerService;     // หัวหน้าตัวจริงที่ถูกสอบ

    // ① สร้างเทรนเนอร์ → ต้องได้ซองขาออกที่มี id กลับมา
    @Test
    void createTrainer_returnsResponse() {
        // Given
        TrainerRequestDTO dto = new TrainerRequestDTO("โค้ชเอ", "0812345678", "เวท");
        Trainer trainer = new Trainer(null, "โค้ชเอ", "0812345678", "เวท");
        Trainer savedTrainer = new Trainer(1L, "โค้ชเอ", "0812345678", "เวท");
        TrainerResponseDTO response = new TrainerResponseDTO(1L, "โค้ชเอ", "0812345678", "เวท");

        when(trainerMapper.toEntity(dto)).thenReturn(trainer);
        when(trainerRepository.save(trainer)).thenReturn(savedTrainer);
        when(trainerMapper.toResponse(savedTrainer)).thenReturn(response);

        // When
        TrainerResponseDTO result = trainerService.createTrainer(dto);

        // Then
        assertEquals(1L, result.getTrainerId());
        assertEquals("โค้ชเอ", result.getName());
        verify(trainerRepository).save(trainer);
    }

    // ② หาเทรนเนอร์ที่มีอยู่ → ต้องได้ข้อมูลคนนั้น
    @Test
    void getTrainerById_found_returnsResponse() {
        // Given
        Trainer trainer = new Trainer(1L, "โค้ชเอ", "0812345678", "เวท");
        TrainerResponseDTO response = new TrainerResponseDTO(1L, "โค้ชเอ", "0812345678", "เวท");

        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));
        when(trainerMapper.toResponse(trainer)).thenReturn(response);

        // When
        TrainerResponseDTO result = trainerService.getTrainerById(1L);

        // Then
        assertEquals(1L, result.getTrainerId());
        assertEquals("โค้ชเอ", result.getName());
    }

    // ③ หาเทรนเนอร์ที่ไม่มี → ต้องโยน 404
    @Test
    void getTrainerById_notFound_throws404() {
        // Given
        when(trainerRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> trainerService.getTrainerById(99L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    // ④ ดูทั้งหมด → จำนวนต้องเท่ากับที่อยู่ใน DB
    @Test
    void getAllTrainers_returnsAll() {
        // Given
        Trainer t1 = new Trainer(1L, "โค้ชเอ", "0812345678", "เวท");
        Trainer t2 = new Trainer(2L, "โค้ชบี", "0898765432", "คาร์ดิโอ");

        when(trainerRepository.findAll()).thenReturn(List.of(t1, t2));
        when(trainerMapper.toResponse(t1)).thenReturn(new TrainerResponseDTO(1L, "โค้ชเอ", "0812345678", "เวท"));
        when(trainerMapper.toResponse(t2)).thenReturn(new TrainerResponseDTO(2L, "โค้ชบี", "0898765432", "คาร์ดิโอ"));

        // When
        List<TrainerResponseDTO> result = trainerService.getAllTrainers();

        // Then
        assertEquals(2, result.size());
        assertEquals("โค้ชบี", result.get(1).getName());
    }

    // ⑤ แก้ไข → ข้อมูลใน Entity ต้องถูกเปลี่ยนเป็นค่าใหม่ก่อน save
    @Test
    void updateTrainer_changesFields() {
        // Given
        Trainer trainer = new Trainer(1L, "โค้ชเอ", "0812345678", "เวท");
        TrainerRequestDTO dto = new TrainerRequestDTO("โค้ชเอ", "0800000000", "โยคะ");
        TrainerResponseDTO response = new TrainerResponseDTO(1L, "โค้ชเอ", "0800000000", "โยคะ");

        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(trainer)).thenReturn(trainer);
        when(trainerMapper.toResponse(trainer)).thenReturn(response);

        // When
        trainerService.updateTrainer(1L, dto);

        // Then
        assertEquals("0800000000", trainer.getPhone());
        assertEquals("โยคะ", trainer.getSpecialty());
        verify(trainerRepository).save(trainer);
    }

    // ⑥ ลบคนที่มีอยู่ → ต้องสั่ง Repository ลบจริง
    @Test
    void deleteTrainer_found_callsDelete() {
        // Given
        Trainer trainer = new Trainer(1L, "โค้ชเอ", "0812345678", "เวท");
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));

        // When
        trainerService.deleteTrainer(1L);

        // Then
        verify(trainerRepository).delete(trainer);
    }

    // ⑦ ลบคนที่ไม่มี → ต้องโยน 404 และห้ามสั่งลบ
    @Test
    void deleteTrainer_notFound_throws404() {
        // Given
        when(trainerRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        assertThrows(ResponseStatusException.class, () -> trainerService.deleteTrainer(99L));
        verify(trainerRepository, never()).delete(any());
    }
}