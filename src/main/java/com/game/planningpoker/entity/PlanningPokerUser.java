package com.game.planningpoker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
public class PlanningPokerUser {

    @Id
    @GeneratedValue
    private UUID id;
    @NotEmpty(message = "name is mandatory.")
    private String name;
    @NotEmpty(message = "nickName is mandatory.")
    private String nickName;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade({CascadeType.DELETE, CascadeType.PERSIST})
    Set<UserStory> votedStories;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanningPokerUser user = (PlanningPokerUser) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(nickName, user.nickName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nickName);
    }
}
