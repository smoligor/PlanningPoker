package com.game.planningpoker.repository;

import com.game.planningpoker.entity.UserStory;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserStoryRepo extends CrudRepository<UserStory, UUID> {
}
