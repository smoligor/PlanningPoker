package com.game.planningpoker.service;

import com.game.planningpoker.dto.CreateUserStoryRequest;
import com.game.planningpoker.dto.CreateUserStoryResponse;
import com.game.planningpoker.dto.StoryVoteStatusResponse;
import com.game.planningpoker.dto.VoteForStoryRequest;

import java.util.UUID;

public interface UserStoryService {

    CreateUserStoryResponse createUserStory(CreateUserStoryRequest createUserStoryRequest);

    //UserStoryInfo getBySessionIdAndUserIdAndStoryId(UUID sessionId, UUID userId, UUID storyId);

    void voteForStory(VoteForStoryRequest voteForStoryRequest);

    StoryVoteStatusResponse getStoryVoteStatus(UUID userId, UUID storyId, UUID sessionId);

    void endVoting(UUID userStoryId, UUID userId);

    void startVoting(UUID userStoryId, UUID userId, UUID sessionId);

    void deleteStory(UUID userId, UUID userStoryId);
}
