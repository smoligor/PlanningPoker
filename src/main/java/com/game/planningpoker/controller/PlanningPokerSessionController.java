package com.game.planningpoker.controller;

import com.game.planningpoker.dto.CreateSessionRequest;
import com.game.planningpoker.dto.CreateSessionResponse;
import com.game.planningpoker.service.PlanningPokerSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/session")
public class PlanningPokerSessionController {

    private PlanningPokerSessionService planningPokerSessionService;


    @PostMapping("/create")
    ResponseEntity<CreateSessionResponse> createPlanningPokerSession(@RequestBody CreateSessionRequest sessionDto) {
        return ResponseEntity.ok(planningPokerSessionService.createSession(sessionDto));
    }

    @PutMapping("/join/sessionId/{sessionId}/userId/{userId}")
    ResponseEntity<String> joinSession(@PathVariable UUID sessionId, @PathVariable UUID userId) {
        planningPokerSessionService.joinSession(sessionId, userId);
        return ResponseEntity.ok("User successfully joined to session.");
    }

    @DeleteMapping("/sessionId/{sessionId}/userId/{userId}")
    ResponseEntity<String> destroySession(@PathVariable UUID sessionId, @PathVariable UUID userId) {
        planningPokerSessionService.destroySession(sessionId, userId);
        return ResponseEntity.ok("User successfully deleted the session.");
    }

    public PlanningPokerSessionController(PlanningPokerSessionService planningPokerSessionService) {
        this.planningPokerSessionService = planningPokerSessionService;
    }

}
