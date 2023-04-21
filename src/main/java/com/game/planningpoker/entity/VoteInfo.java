package com.game.planningpoker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class VoteInfo {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID storyId;

    private UUID userId;

    private long voteValue;
}
