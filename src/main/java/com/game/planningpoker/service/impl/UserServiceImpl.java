package com.game.planningpoker.service.impl;

import com.game.planningpoker.dto.CreateUserRequest;
import com.game.planningpoker.dto.CreateUserResponse;
import com.game.planningpoker.entity.PlanningPokerUser;
import com.game.planningpoker.repository.UserRepo;
import com.game.planningpoker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;

    @Override
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
        PlanningPokerUser user = new PlanningPokerUser();
        BeanUtils.copyProperties(createUserRequest, user);
        PlanningPokerUser savedUser = userRepo.save(user);
        CreateUserResponse createUserResponse = new CreateUserResponse();
        createUserResponse.setUserId(savedUser.getId());
        createUserResponse.setMessage("User successfully created.");
        log.info("User created: " + savedUser);
        return createUserResponse;
    }

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
}
