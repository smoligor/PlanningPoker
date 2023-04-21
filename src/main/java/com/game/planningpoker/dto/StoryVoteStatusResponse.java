package com.game.planningpoker.dto;

import com.game.planningpoker.enumeration.UserStoryStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class StoryVoteStatusResponse {

    private UUID storyId;

    private UserStoryStatus storyStatus;

    private long emittedVotes;

    List<VoteValueInfo> voteValueInfo = new ArrayList<>();
}
