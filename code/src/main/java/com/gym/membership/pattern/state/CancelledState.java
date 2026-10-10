package com.gym.membership.pattern.state;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

public class CancelledState implements SessionState {

    @Override
    public String complete(){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot complete a cancelled session");
    }

    @Override
    public String cancel() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot cancel a cancelled session");
    }

}
