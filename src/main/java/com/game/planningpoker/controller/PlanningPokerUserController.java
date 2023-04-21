package com.game.planningpoker.controller;

import com.game.planningpoker.dto.CreateUserRequest;
import com.game.planningpoker.dto.CreateUserResponse;
import com.game.planningpoker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class PlanningPokerUserController {

    private UserService userService;

    @PostMapping("")
    ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        userService.createUser(createUserRequest);
        return ResponseEntity.ok(userService.createUser(createUserRequest));
    }

    public PlanningPokerUserController(UserService userService) {
        this.userService = userService;
    }
}
