package com.game.planningpoker.repository;

import com.game.planningpoker.entity.PlanningPokerSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlanningPokerSessionRepo extends CrudRepository<PlanningPokerSession, UUID> {
}
