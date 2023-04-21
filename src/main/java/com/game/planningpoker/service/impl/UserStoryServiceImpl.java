package com.game.planningpoker.service.impl;

import com.game.planningpoker.dto.CreateUserStoryRequest;
import com.game.planningpoker.dto.CreateUserStoryResponse;
import com.game.planningpoker.dto.StoryVoteStatusResponse;
import com.game.planningpoker.dto.VoteForStoryRequest;
import com.game.planningpoker.dto.VoteValueInfo;
import com.game.planningpoker.entity.PlanningPokerSession;
import com.game.planningpoker.entity.PlanningPokerUser;
import com.game.planningpoker.entity.UserStory;
import com.game.planningpoker.entity.VoteInfo;
import com.game.planningpoker.enumeration.UserStoryStatus;
import com.game.planningpoker.exception.PlanningPokerException;
import com.game.planningpoker.repository.PlanningPokerSessionRepo;
import com.game.planningpoker.repository.UserRepo;
import com.game.planningpoker.repository.UserStoryRepo;
import com.game.planningpoker.repository.VoteInfoRepo;
import com.game.planningpoker.service.UserStoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserStoryServiceImpl implements UserStoryService {

    private UserStoryRepo userStoryRepo;
    private PlanningPokerSessionRepo sessionRepo;

    private UserRepo userRepo;

    private VoteInfoRepo voteInfoRepo;

    @Override
    public CreateUserStoryResponse createUserStory(CreateUserStoryRequest createUserStoryRequest) {
        UserStory userStory = new UserStory();
        BeanUtils.copyProperties(createUserStoryRequest, userStory);
        userStory.setStatus(UserStoryStatus.PENDING);
        userStoryRepo.save(userStory);
        CreateUserStoryResponse createUserStoryResponse = new CreateUserStoryResponse();
        createUserStoryResponse.setId(userStory.getId());
        createUserStoryResponse.setMessage("Story created.");
        log.info("Story created " + userStory);
        return createUserStoryResponse;
    }

    @Override
    @Transactional
    public void voteForStory(VoteForStoryRequest voteForStoryRequest) {
        Optional<UserStory> userStoryOptional = userStoryRepo.findById(voteForStoryRequest.getStoryId());
        UserStory userStory = userStoryOptional
            .orElseThrow(() -> new PlanningPokerException("Story with id " + voteForStoryRequest.getStoryId() + " not found."));
        userStory.setEmittedVotes(userStory.getEmittedVotes() + 1);
        Optional<PlanningPokerSession> sessionOptional = sessionRepo.findById(voteForStoryRequest.getSessionId());
        List<UUID> ids = getUuids(voteForStoryRequest, sessionOptional);
        if (!ids.contains(voteForStoryRequest.getStoryId())) {
            throw new PlanningPokerException("User story must be attached to a session.");
        }
        saveVoteInfo(voteForStoryRequest);

        validateParticipating(voteForStoryRequest, sessionOptional);

        Optional<PlanningPokerUser> userOptional = userRepo.findById(voteForStoryRequest.getUserId());
        PlanningPokerUser planningPokerUser = userOptional
            .orElseThrow(() -> new PlanningPokerException("User with id " + voteForStoryRequest.getUserId() + " not found."));
        if (planningPokerUser.getVotedStories().contains(userStory)) {
            throw new PlanningPokerException("It is impossible to vote for a story more than one time.");
        }
        planningPokerUser.getVotedStories().add(userStory);
        userRepo.save(planningPokerUser);
    }

    private void validateParticipating(VoteForStoryRequest voteForStoryRequest, Optional<PlanningPokerSession> sessionOptional) {
        PlanningPokerUser user = userRepo.findById(voteForStoryRequest.getUserId())
            .orElseThrow(() -> new PlanningPokerException("User with id " + voteForStoryRequest.getUserId() + " not found."));
        if (!sessionOptional.get().getParticipatingPlanningPokerUsers().contains(user)) {
            throw new PlanningPokerException("Only user participating in a session can vote for a user story.");
        }
    }

    private static List<UUID> getUuids(VoteForStoryRequest voteForStoryRequest, Optional<PlanningPokerSession> sessionOptional) {
        PlanningPokerSession session = sessionOptional.
            orElseThrow(() -> new PlanningPokerException("Session with id " + voteForStoryRequest.getSessionId() + " not found."));
        session.getAttachedUserStories();
        return session.getAttachedUserStories().stream().map(UserStory::getId).toList();
    }

    private void saveVoteInfo(VoteForStoryRequest voteForStoryRequest) {
        VoteInfo voteInfo = new VoteInfo();
        voteInfo.setVoteValue(voteForStoryRequest.getVoteValue());
        voteInfo.setStoryId(voteForStoryRequest.getStoryId());
        voteInfo.setUserId(voteForStoryRequest.getUserId());
        voteInfoRepo.save(voteInfo);
    }

    @Override
    public StoryVoteStatusResponse getStoryVoteStatus(UUID userId, UUID userStoryId, UUID sessionId) {
        Optional<UserStory> userStoryOptional = userStoryRepo.findById(userStoryId);
        StoryVoteStatusResponse storyVoteStatusResponse = new StoryVoteStatusResponse();
        UserStory userStory = userStoryOptional
            .orElseThrow(() -> new PlanningPokerException("User story with id " + userStoryId + " not found."));
        storyVoteStatusResponse.setStoryStatus(userStory.getStatus());
        storyVoteStatusResponse.setStoryId(userStoryId);
        storyVoteStatusResponse.setEmittedVotes(userStory.getEmittedVotes());
        if (userStory.getStatus().equals(UserStoryStatus.VOTING)) {
            return getResponseForVoting(userId, userStoryId, storyVoteStatusResponse);
        }
        sessionRepo.findById(sessionId).orElseThrow(() -> new PlanningPokerException("Cant find session with such id" + sessionId))
            .getParticipatingPlanningPokerUsers().forEach(user -> {

            });
        Optional<List<VoteInfo>> optionalVoteInfos = voteInfoRepo.findAllByStoryId(userStoryId);
        List<VoteInfo> voteInfoList = optionalVoteInfos
            .orElseThrow(() -> new PlanningPokerException("Can't find vote information for story"));
        voteInfoList.forEach(e -> {
            VoteValueInfo voteValueInfo = new VoteValueInfo();
            voteValueInfo.setUserId(e.getUserId());
            voteValueInfo.setVoteValue(e.getVoteValue());
            storyVoteStatusResponse.getVoteValueInfo().add(voteValueInfo);
        });
        return storyVoteStatusResponse;
    }

    @Override
    @Transactional
    public void endVoting(UUID userStoryId, UUID userId) {
        Optional<UserStory> userStoryOptional = userStoryRepo.findById(userStoryId);
        UserStory userStory = userStoryOptional
            .orElseThrow(() -> new PlanningPokerException("Story with id " + userStoryId + " not found."));

        if (!userStory.getStatus().equals(UserStoryStatus.VOTING)) {
            throw new PlanningPokerException("To end voting story status must be VOTING.");
        }
        userStory.setStatus(UserStoryStatus.VOTED);
        userStoryRepo.save(userStory);
        log.info("Voting ended for story : " + userStory);
    }

    @Override
    @Transactional
    public void startVoting(UUID userStoryId, UUID userId, UUID sessionId) {
        Optional<UserStory> userStoryOptional = userStoryRepo.findById(userStoryId);
        UserStory userStory = userStoryOptional
            .orElseThrow(() -> new PlanningPokerException("Story with id " + userStoryId + " not found."));
        userStory.setStatus(UserStoryStatus.VOTING);
        Optional<PlanningPokerSession> optional = sessionRepo.findById(sessionId);
        PlanningPokerSession session = optional.orElseThrow(() -> new PlanningPokerException("Can't find session with id " + sessionId));
        session.getAttachedUserStories().add(userStory);
        sessionRepo.save(session);
        userStoryRepo.save(userStory);
        log.info("Voting started for story : " + userStory);
    }

    @Override
    @Transactional
    public void deleteStory(UUID userId, UUID userStoryId) {

        Optional<UserStory> userStoryOptional = userStoryRepo.findById(userStoryId);
        UserStory userStory = userStoryOptional
            .orElseThrow(() -> new PlanningPokerException("Story with id " + userStoryId + " not found."));
        if (!userStory.getStatus().equals(UserStoryStatus.PENDING)) {
            throw new PlanningPokerException("Story must be in pending status in order to be deleted.");
        }
        Optional<VoteInfo> voteInfoOptional = voteInfoRepo.findByUserIdAndStoryId(userId, userStoryId);
        VoteInfo voteInfo = voteInfoOptional.orElseThrow(() -> new PlanningPokerException("Vote info not found."));
        voteInfoRepo.delete(voteInfo);
        userStoryRepo.delete(userStory);
        log.info("Story deleted : " + userStory);
    }

    private StoryVoteStatusResponse getResponseForVoting(UUID userId, UUID userStoryId,
                                                         StoryVoteStatusResponse storyVoteStatusResponse) {
        VoteValueInfo voteValueInfo = new VoteValueInfo();
        voteValueInfo.setUserId(userId);
        Optional<VoteInfo> optional = voteInfoRepo.findByUserIdAndStoryId(userId, userStoryId);
        VoteInfo voteInfo = optional.orElseThrow(() -> new PlanningPokerException("Can't find vote info."));
        voteValueInfo.setVoteValue(voteInfo.getVoteValue());
        storyVoteStatusResponse.getVoteValueInfo().add(voteValueInfo);
        return storyVoteStatusResponse;
    }

    public UserStoryServiceImpl(UserStoryRepo userStoryRepo, PlanningPokerSessionRepo sessionRepo, UserRepo userRepo, VoteInfoRepo voteInfoRepo) {
        this.userStoryRepo = userStoryRepo;
        this.sessionRepo = sessionRepo;
        this.userRepo = userRepo;
        this.voteInfoRepo = voteInfoRepo;
    }
}
