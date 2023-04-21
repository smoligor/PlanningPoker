package com.game.planningpoker.controller;

import com.game.planningpoker.dto.CreateUserStoryRequest;
import com.game.planningpoker.dto.CreateUserStoryResponse;
import com.game.planningpoker.dto.StartVotingRequest;
import com.game.planningpoker.dto.StoryVoteStatusResponse;
import com.game.planningpoker.dto.VoteForStoryRequest;
import com.game.planningpoker.service.UserStoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/userStory")
public class UserStoryController {

    private UserStoryService userStoryService;

    @PostMapping()
    public ResponseEntity<CreateUserStoryResponse> createUserStory(@RequestBody CreateUserStoryRequest createUserStoryRequest) {
        CreateUserStoryResponse userStoryResponse = userStoryService.createUserStory(createUserStoryRequest);
        return ResponseEntity.ok(userStoryResponse);
    }

    @PostMapping("/vote")
    public ResponseEntity<String> voteForStory(@RequestBody VoteForStoryRequest voteForStoryRequest) {
        userStoryService.voteForStory(voteForStoryRequest);
        return ResponseEntity.ok("User successfully voted for a story.");
    }

    @GetMapping("/voteStatus/userId/{userId}/storyId/{storyId}/sessionId/{sessionId}")
    public ResponseEntity<StoryVoteStatusResponse> getVoteStatus(@PathVariable UUID userId,
                                                                 @PathVariable UUID storyId,
                                                                 @PathVariable UUID sessionId) {
        return ResponseEntity.ok(userStoryService.getStoryVoteStatus(userId, storyId, sessionId));
    }

    @PostMapping("/startVoting")
    public ResponseEntity<String> startVoting(@RequestBody StartVotingRequest request) {
        userStoryService.startVoting(request.getUserStoryId(), request.getUserId(), request.getSessionId());
        return ResponseEntity.ok("Voting started.");
    }

    @PostMapping("/endVoting/userStoryId/{userStoryId}/userId/{userId}")
    public ResponseEntity<String> endVoting(@PathVariable UUID userStoryId, @PathVariable UUID userId) {
        userStoryService.endVoting(userStoryId, userId);
        return ResponseEntity.ok("Voting ended.");
    }

    @DeleteMapping("/userId/{userId}/userStoryId/{userStoryId}")
    public ResponseEntity<String> deleteStory(@PathVariable UUID userStoryId, @PathVariable UUID userId) {
        userStoryService.deleteStory(userId, userStoryId);
        return ResponseEntity.ok("Story deleted.");
    }

    public UserStoryController(UserStoryService userStoryService) {
        this.userStoryService = userStoryService;
    }
}
