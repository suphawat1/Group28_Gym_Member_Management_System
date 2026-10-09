package com.gym.membership.pattern.observer;

import com.gym.membership.domain.entity.TrainingSession;

import org.springframework.stereotype.Component;

@Component
public class TrainerNotifier implements SessionObserver {
    @Override
    public void onStatusChanged(TrainingSession session) {
        System.out.println("[Trainer] " + session.getTrainerId().getName() + ": session #"+session.getSessionId()+ " is now "+session.getStatus());
    }
}
