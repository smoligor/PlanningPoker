package com.game.planningpoker.service.impl;

import com.game.planningpoker.dto.CreateSessionRequest;
import com.game.planningpoker.dto.CreateSessionResponse;
import com.game.planningpoker.entity.PlanningPokerSession;
import com.game.planningpoker.entity.PlanningPokerUser;
import com.game.planningpoker.entity.VoteInfo;
import com.game.planningpoker.exception.PlanningPokerException;
import com.game.planningpoker.repository.PlanningPokerSessionRepo;
import com.game.planningpoker.repository.UserRepo;
import com.game.planningpoker.repository.VoteInfoRepo;
import com.game.planningpoker.service.PlanningPokerSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PlanningPokerServiceSessionImpl implements PlanningPokerSessionService {

    private PlanningPokerSessionRepo sessionRepo;
    private UserRepo userRepo;

    private VoteInfoRepo voteInfoRepo;

    @Value("${site.url}")
    private String siteUrl;

    public CreateSessionResponse createSession(CreateSessionRequest dto) {
        PlanningPokerSession planningPokerSession = new PlanningPokerSession();
        BeanUtils.copyProperties(dto, planningPokerSession);
        planningPokerSession.setInviteLink(generateInviteLink());
        PlanningPokerSession saved = sessionRepo.save(planningPokerSession);
        log.info("Session created: " + saved);
        CreateSessionResponse createSessionResponse = new CreateSessionResponse();
        createSessionResponse.setSessionId(saved.getId().toString());

        createSessionResponse.setMessage("Session created.");
        return createSessionResponse;
    }

    @Override
    @Transactional
    public void joinSession(UUID sessionId, UUID userId) {
        Optional<PlanningPokerSession> sessionOptional = sessionRepo.findById(sessionId);
        PlanningPokerSession session =
            sessionOptional.orElseThrow(() -> new PlanningPokerException("Session with id " + sessionId + " not found."));
        Optional<PlanningPokerUser> userOptional = userRepo.findById(userId);
        PlanningPokerUser planningPokerUser = userOptional.orElseThrow(() -> new PlanningPokerException("User with id " + userId + "not found."));
        session.getParticipatingPlanningPokerUsers().add(planningPokerUser);
        sessionRepo.save(session);
        log.info("Used with id" + userId + " joined session " + session);
    }

    @Override
    @Transactional
    public void destroySession(UUID sessionId, UUID userId) {
        Optional<PlanningPokerSession> sessionOptional = sessionRepo.findById(sessionId);
        PlanningPokerSession session = sessionOptional
            .orElseThrow(() -> new PlanningPokerException("Session with id " + sessionId + "not found."));
        userRepo.deleteAll(session.getParticipatingPlanningPokerUsers());
        session.getAttachedUserStories().forEach(userStory -> {
            Optional<List<VoteInfo>> allByStoryId = voteInfoRepo.findAllByStoryId(userStory.getId());
            voteInfoRepo.deleteAll(allByStoryId.orElse(new ArrayList<>()));
        });
        sessionRepo.delete(session);
        log.info("User with id " + userId + " deleted session " + session);
    }

    private String generateInviteLink() {
        return siteUrl + "/session/join/sessionId/" + UUID.randomUUID();
    }

    public PlanningPokerServiceSessionImpl(PlanningPokerSessionRepo sessionRepo, UserRepo userRepo, VoteInfoRepo voteInfoRepo) {
        this.sessionRepo = sessionRepo;
        this.userRepo = userRepo;
        this.voteInfoRepo = voteInfoRepo;
    }
}
