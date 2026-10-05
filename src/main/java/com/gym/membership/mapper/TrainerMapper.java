package com.gym.membership.mapper;
import org.springframework.stereotype.Component;

import com.gym.membership.domain.entity.Trainer;
import com.gym.membership.dto.TrainerRequestDTO;
import com.gym.membership.dto.TrainerResponseDTO;
@Component
public class TrainerMapper {

    public Trainer toEntity(TrainerRequestDTO dto) {
        Trainer trainer = new Trainer();
        trainer.setName(dto.getName());
        trainer.setPhone(dto.getPhone());
        trainer.setSpecialty(dto.getSpecialty());
        return trainer;
    }

    public TrainerResponseDTO toResponse(Trainer trainer) {
        TrainerResponseDTO trainerResponseDTO = new TrainerResponseDTO();
        trainerResponseDTO.setTrainerId(trainer.getTrainerId());
        trainerResponseDTO.setName(trainer.getName());
        trainerResponseDTO.setPhone(trainer.getPhone());
        trainerResponseDTO.setSpecialty(trainer.getSpecialty());
        return trainerResponseDTO;
    }
}
