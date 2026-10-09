package com.gym.membership.pattern.observer;

import com.gym.membership.domain.entity.TrainingSession;

public interface SessionObserver {
    void onStatusChanged(TrainingSession session);
}
