package com.gym.membership.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainingSessionServiceImpl implements TrainingSessionService {
    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainerRepository trainerRepository;
    private final MemberRepository memberRepository;
    private final TrainingSessionMapper trainingSessionMapper;

    @Override
    public TrainingSessionResponseDTO createTrainingSession(TrainingSessionRequestDTO dto) {
        Trainer trainer = findTrainer(dto.getTrainerId());
        Member member = findMember(dto.getMemberId());

        TrainingSession session = trainingSessionMapper.toEntity(dto);
        session.setTrainer(trainer);
        session.setMember(member);
        session.setStatus("BOOKED");

        TrainingSession savedSession = trainingSessionRepository.save(session);
        return trainingSessionMapper.toResponse(savedSession);
    }

    @Override
    public TrainingSessionResponseDTO getTrainingSessionById(Long id) {
        TrainingSession session = findSession(id);
        return trainingSessionMapper.toResponse(session);
    }

    @Override
    public List<TrainingSessionResponseDTO> getAllTrainingSessions() {
        List<TrainingSession> sessions = trainingSessionRepository.findAll();
        List<TrainingSessionResponseDTO> result = new ArrayList<>();
        for (TrainingSession session : sessions) {
            result.add(trainingSessionMapper.toResponse(session));
        }
        return result;
    }

    @Override
    public TrainingSessionResponseDTO updateTrainingSession(Long id, TrainingSessionRequestDTO dto) {
        TrainingSession session = findSession(id);
        session.setTrainer(findTrainer(dto.getTrainerId()));
        session.setMember(findMember(dto.getMemberId()));
        session.setSessionDate(dto.getSessionDate());
        session.setSessionTime(dto.getSessionTime());

        TrainingSession savedSession = trainingSessionRepository.save(session);
        return trainingSessionMapper.toResponse(savedSession);
    }

    @Override
    public void deleteTrainingSession(Long id) {
        TrainingSession session = findSession(id);
        trainingSessionRepository.delete(session);
    }

    private TrainingSession findSession(Long id) {
        return trainingSessionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Training session not found"));
    }

    private Trainer findTrainer(Long id) {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
    }
}
