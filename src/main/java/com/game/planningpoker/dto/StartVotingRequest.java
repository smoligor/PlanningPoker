package com.game.planningpoker.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class StartVotingRequest {

    private UUID userStoryId;

    private UUID userId;

    private UUID sessionId;

}
