package com.game.planningpoker.exception;

import lombok.Data;

@Data
public class PlanningPokerException extends RuntimeException {

    private String message;

    public PlanningPokerException(String message) {
        super(message);
        this.message = message;
    }
}
