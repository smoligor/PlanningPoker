package com.game.planningpoker.repository;

import com.game.planningpoker.entity.PlanningPokerUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepo extends CrudRepository<PlanningPokerUser, UUID> {
}
