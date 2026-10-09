package com.gym.membership.pattern.observer;

import org.springframework.stereotype.Component;

import com.gym.membership.domain.entity.TrainingSession;

@Component
public class MemberNotifier implements SessionObserver {

    @Override
    public void onStatusChanged(TrainingSession session) {
        System.out.println("[Member] " + session.getMemberId().getUsername() + ": session #"+session.getSessionId()+ " is now "+session.getStatus());
    }
}
