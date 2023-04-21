package com.game.planningpoker.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class VoteValueInfo {

    private UUID userId;

    private long voteValue;

}
