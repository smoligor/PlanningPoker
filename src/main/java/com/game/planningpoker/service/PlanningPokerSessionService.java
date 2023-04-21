package com.game.planningpoker.service;

import com.game.planningpoker.dto.CreateSessionRequest;
import com.game.planningpoker.dto.CreateSessionResponse;

import java.util.UUID;

public interface PlanningPokerSessionService {

    CreateSessionResponse createSession(CreateSessionRequest dto);

    void joinSession(UUID sessionId, UUID userId);

    void destroySession(UUID sessionId, UUID userId);
}
