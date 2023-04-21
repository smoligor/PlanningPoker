package com.game.planningpoker.service;

import com.game.planningpoker.dto.CreateUserRequest;
import com.game.planningpoker.dto.CreateUserResponse;

public interface UserService {

    CreateUserResponse createUser(CreateUserRequest createUserRequest);

}
