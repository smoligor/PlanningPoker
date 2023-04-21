package com.game.planningpoker.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateUserStoryResponse {

    private UUID id;

    private String message;

}
