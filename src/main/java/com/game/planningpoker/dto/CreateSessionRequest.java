package com.game.planningpoker.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Data
public class CreateSessionRequest {

    @NotEmpty(message = "title is mandatory")
    private String title;

    @NotEmpty(message = "deckType is mandatory")
    private String deckType;
}
