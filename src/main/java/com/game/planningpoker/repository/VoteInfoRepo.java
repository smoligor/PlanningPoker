package com.game.planningpoker.repository;

import com.game.planningpoker.entity.VoteInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VoteInfoRepo extends CrudRepository<VoteInfo, UUID> {

    Optional<VoteInfo> findByUserIdAndStoryId(UUID userId, UUID storyId);

    Optional<List<VoteInfo>> findAllByStoryId(UUID storyId);
}
