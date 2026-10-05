package com.gym.membership.service;

import java.util.List;

import com.gym.membership.dto.TrainerRequestDTO;
import com.gym.membership.dto.TrainerResponseDTO;

public interface TrainerService {
    
    TrainerResponseDTO createTrainer(TrainerRequestDTO dto);

    TrainerResponseDTO getTrainerById(Long id);

    List<TrainerResponseDTO> getAllTrainers();
    
    TrainerResponseDTO updateTrainer(Long id,TrainerRequestDTO trainerRequestDTO);

    void deleteTrainer(Long id);
}
