package com.gym.membership.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.membership.domain.entity.Trainer;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    
}
