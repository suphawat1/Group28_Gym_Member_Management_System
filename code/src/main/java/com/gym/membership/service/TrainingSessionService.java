package com.gym.membership.service;

import java.util.List;

import com.gym.membership.dto.TrainingSessionRequestDTO;
import com.gym.membership.dto.TrainingSessionResponseDTO;

public interface TrainingSessionService {
    
    TrainingSessionResponseDTO createTrainingSession(TrainingSessionRequestDTO dto);

    TrainingSessionResponseDTO getTrainingSessionById(Long id);

    List<TrainingSessionResponseDTO> getAllTrainingSessions();
    
    TrainingSessionResponseDTO updateTrainingSession(Long id,TrainingSessionRequestDTO sessionRequestDTO);

    void deleteTrainingSession(Long id);
}
