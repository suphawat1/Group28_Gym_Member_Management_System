package com.gym.membership.pattern.state;

import com.gym.membership.domain.entity.TrainingSession;
import com.gym.membership.domain.repository.TrainingSessionRepository;
import com.gym.membership.dto.TrainingSessionResponseDTO;
import com.gym.membership.mapper.TrainingSessionMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class SessionStatusService {
    private final Map<String, SessionState> states;
    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainingSessionMapper trainingSessionMapper;

    public TrainingSessionResponseDTO completeSession(Long sessionId){
        TrainingSession session = trainingSessionRepository.findById(sessionId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
        SessionState state = states.get(session.getStatus());
        session.setStatus(state.complete());
        TrainingSession savedSession = trainingSessionRepository.save(session);
        return trainingSessionMapper.toResponse(savedSession);
    }
}
