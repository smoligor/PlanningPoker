package com.game.planningpoker.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class CreateUserStoryRequest {

    private String name;

    private String description;

    private UUID sessionId;

    private UUID userId;

}
