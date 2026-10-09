package com.gym.membership.pattern.state;

import org.springframework.stereotype.Component;

@Component("BOOKED")
public class BookedState implements SessionState{
    @Override
    public String complete(){
        return "COMPLETED";
    }

    @Override
    public String cancel() {
        return "CANCELLED";
    }
}
