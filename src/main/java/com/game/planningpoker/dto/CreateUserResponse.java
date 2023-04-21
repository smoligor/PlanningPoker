package com.game.planningpoker.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateUserResponse {

    private UUID userId;
    private String message;

}
