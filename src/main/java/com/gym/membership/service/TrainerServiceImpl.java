package com.gym.membership.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gym.membership.domain.entity.Trainer;
import com.gym.membership.domain.repository.TrainerRepository;
import com.gym.membership.dto.TrainerRequestDTO;
import com.gym.membership.dto.TrainerResponseDTO;
import com.gym.membership.mapper.TrainerMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;

    @Override
    public TrainerResponseDTO createTrainer(TrainerRequestDTO dto) {
        Trainer trainer = trainerMapper.toEntity(dto);
        Trainer savedTrainer = trainerRepository.save(trainer);
        return trainerMapper.toResponse(savedTrainer);
    }

    @Override
    public TrainerResponseDTO getTrainerById(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));
        return trainerMapper.toResponse(trainer);
    }

    @Override
    public List<TrainerResponseDTO> getAllTrainers() {
        List<Trainer> trainers = trainerRepository.findAll();
        List<TrainerResponseDTO> result = new ArrayList<>();
        for (Trainer trainer : trainers) {
            result.add(trainerMapper.toResponse(trainer));
        }
        return result;
    }

    @Override
    public TrainerResponseDTO updateTrainer(Long id, TrainerRequestDTO dto) {
        Trainer trainer = trainerRepository.findById(id)// ขั้น 1: copy มาจาก getTrainerById
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));
        trainer.setName(dto.getName());            // ขั้น 2 (ทำให้ดู 1 บรรทัด)
        trainer.setPhone(dto.getPhone());                   // phone
        trainer.setSpecialty(dto.getSpecialty());                  // specialty

        Trainer savedTrainer = trainerRepository.save(trainer);       // ขั้น 3
        return  trainerMapper.toResponse(savedTrainer);                     // ขั้น 4
    }

    @Override
    public void deleteTrainer(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));
        trainerRepository.delete(trainer);
    }
}
