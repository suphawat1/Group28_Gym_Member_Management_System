package com.gym.membership.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.membership.domain.entity.TrainingSession;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long>{
    
}
