package com.game.planningpoker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.List;
import java.util.UUID;


@Entity
@Data
public class PlanningPokerSession {

    @Id
    @GeneratedValue
    private UUID id;

    @NotEmpty(message = "title is mandatory.")
    private String title;

    @NotEmpty(message = "deckType is mandatory.")
    private String deckType;

    private String inviteLink;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade({CascadeType.DELETE, CascadeType.PERSIST})
    private List<PlanningPokerUser> participatingPlanningPokerUsers;

    @OneToMany(fetch = FetchType.EAGER)
    @Cascade({CascadeType.DELETE, CascadeType.PERSIST})
    private List<UserStory> attachedUserStories;

}
