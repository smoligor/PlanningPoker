package com.game.planningpoker.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserStoryInfo {

    private UUID userStoryId;

    private VoteValueInfo voteValueInfo;

}
