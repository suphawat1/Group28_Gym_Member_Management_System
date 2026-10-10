package com.gym.membership.pattern.state;

import com.gym.membership.domain.entity.TrainingSession;
import com.gym.membership.domain.repository.TrainingSessionRepository;
import com.gym.membership.dto.TrainingSessionResponseDTO;
import com.gym.membership.mapper.TrainingSessionMapper;
import com.gym.membership.pattern.observer.SessionObserver;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SessionStatusService {
    private final Map<String, SessionState> states;
    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainingSessionMapper trainingSessionMapper;
    private final List<SessionObserver> observers;                      // ① ใหม่

    public TrainingSessionResponseDTO completeSession(Long sessionId){
        TrainingSession session = trainingSessionRepository.findById(sessionId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
        SessionState state = states.get(session.getStatus());
        session.setStatus(state.complete());
        TrainingSession savedSession = trainingSessionRepository.save(session);
        notifyObservers(savedSession);                                  // ③ ใหม่
        return trainingSessionMapper.toResponse(savedSession);
    }

    public TrainingSessionResponseDTO cancelSession(Long sessionId){
        TrainingSession session = trainingSessionRepository.findById(sessionId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
        SessionState state = states.get(session.getStatus());
        session.setStatus(state.cancel());
        TrainingSession savedSession = trainingSessionRepository.save(session);
        notifyObservers(savedSession);                                  // ③ ใหม่
        return trainingSessionMapper.toResponse(savedSession);
    }

    private void notifyObservers(TrainingSession session) {             // ② ใหม่
        for (SessionObserver observer : observers) {
            observer.onStatusChanged(session);
        }
    }
}