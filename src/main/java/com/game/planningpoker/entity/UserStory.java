package com.game.planningpoker.entity;

import com.game.planningpoker.enumeration.UserStoryStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Objects;
import java.util.UUID;

@Entity
@Data
public class UserStory {
    @Id
    @GeneratedValue
    private UUID id;
    @NotEmpty(message = "name is mandatory.")
    private String name;
    private UserStoryStatus status;

    private int emittedVotes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserStory userStory = (UserStory) o;
        return id == userStory.id && name.equals(userStory.name) && status == userStory.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status);
    }
}
