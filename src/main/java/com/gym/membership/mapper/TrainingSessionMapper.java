package com.gym.membership.mapper;
import org.springframework.stereotype.Component;

import com.gym.membership.domain.entity.TrainingSession;
import com.gym.membership.dto.TrainingSessionRequestDTO;
import com.gym.membership.dto.TrainingSessionResponseDTO;
@Component
public class TrainingSessionMapper {

    public TrainingSession toEntity(TrainingSessionRequestDTO dto) {
        TrainingSession session = new TrainingSession();
        session.setSessionDate(dto.getSessionDate());
        session.setSessionTime(dto.getSessionTime());
        return session;
    }

    public TrainingSessionResponseDTO toResponse(TrainingSession session) {
        TrainingSessionResponseDTO sessionResponseDTO = new TrainingSessionResponseDTO();
        sessionResponseDTO.setSessionId(session.getSessionId());
        sessionResponseDTO.setTrainerId(session.getTrainer().getTrainerId());
        sessionResponseDTO.setMemberId(session.getMember().getMemberId());
        sessionResponseDTO.setSessionDate(session.getSessionDate());
        sessionResponseDTO.setSessionTime(session.getSessionTime());
        sessionResponseDTO.setStatus(session.getStatus());
        return sessionResponseDTO;
    }
}
