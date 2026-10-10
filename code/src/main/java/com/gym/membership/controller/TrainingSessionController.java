package com.gym.membership.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gym.membership.dto.TrainingSessionRequestDTO;
import com.gym.membership.dto.TrainingSessionResponseDTO;
import com.gym.membership.pattern.state.SessionStatusService;
import com.gym.membership.service.TrainingSessionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/training-sessions")
@RequiredArgsConstructor
public class TrainingSessionController {
    private final TrainingSessionService sessionService;
    private final SessionStatusService sessionStatusService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrainingSessionResponseDTO createTrainingSession(@Valid @RequestBody TrainingSessionRequestDTO dto) {
        return sessionService.createTrainingSession(dto);
    }

    @GetMapping
    public List<TrainingSessionResponseDTO> getAllTrainingSessions() {
        return sessionService.getAllTrainingSessions();
    }

    @GetMapping("/{id}")
    public TrainingSessionResponseDTO getTrainingSessionById(@PathVariable Long id) {
        return sessionService.getTrainingSessionById(id);
    }

    @PutMapping("/{id}")
    public TrainingSessionResponseDTO updateTrainingSession(@PathVariable Long id,@Valid @RequestBody TrainingSessionRequestDTO dto){
        return sessionService.updateTrainingSession(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrainingSession(@PathVariable Long id){
        sessionService.deleteTrainingSession(id);
    }

    @PostMapping("/{id}/complete")
    public TrainingSessionResponseDTO completeSession(@PathVariable Long id){
        return sessionStatusService.completeSession(id);
    }
    
    @PostMapping("/{id}/cancel")
    public TrainingSessionResponseDTO cancelSession(@PathVariable Long id) {
        return sessionStatusService.cancelSession(id);
    }
}
