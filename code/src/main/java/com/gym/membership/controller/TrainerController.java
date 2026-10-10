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

import com.gym.membership.dto.TrainerRequestDTO;
import com.gym.membership.dto.TrainerResponseDTO;
import com.gym.membership.service.TrainerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrainerResponseDTO createTrainer(@Valid @RequestBody TrainerRequestDTO dto) {
        return trainerService.createTrainer(dto);
    }

    @GetMapping
    public List<TrainerResponseDTO> getAllTrainers() {
        return trainerService.getAllTrainers();
    }

    @GetMapping("/{id}")
    public TrainerResponseDTO getTrainerById(@PathVariable Long id) {
        return trainerService.getTrainerById(id);
    }

    @PutMapping("/{id}")
    public TrainerResponseDTO updateTrainer(@PathVariable Long id,@Valid @RequestBody TrainerRequestDTO dto){
        return trainerService.updateTrainer(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrainer(@PathVariable Long id){
        trainerService.deleteTrainer(id);
    }
    
}
