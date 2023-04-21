package com.game.planningpoker.controller;


import com.game.planningpoker.exception.PlanningPokerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class PlanningPokerExceptionHandler {

    @ExceptionHandler(PlanningPokerException.class)
    ResponseEntity<String> planningPokerExceptionHandler(PlanningPokerException planningPokerException) {
        log.error("Exception occurred: " + planningPokerException.getMessage());
        return ResponseEntity.badRequest().body(planningPokerException.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<String> genericExceptionHandler(RuntimeException runtimeException) {
        log.error("Internal server error occurred: " + runtimeException.getMessage());
        runtimeException.printStackTrace();
        return ResponseEntity.internalServerError().body("Something went wrong.");
    }

    @ExceptionHandler(TransactionSystemException.class)
    ResponseEntity<String> validationExceptionHandler(TransactionSystemException ex) {
        log.error("Validation exception occurred: " + ex.getOriginalException().getCause().getMessage());
        return ResponseEntity.internalServerError().body(ex.getOriginalException().getCause().getMessage());
    }
}
