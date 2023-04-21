package com.game.planningpoker.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class VoteForStoryRequest {

    private UUID userId;

    private UUID storyId;

    private UUID sessionId;

    private long voteValue;

}
