package com.equalize.model;

import com.equalize.model.enums.GameType;
import com.equalize.model.enums.PersonalityType;
import com.equalize.model.enums.RoleType;

import java.util.Objects;

/**
 * Represents a single club participant with survey + preference data.
 */
public class Participant extends User {

    private PersonalityType personalityType;
    private int personalityScore;          // 0–100 after scaling
    private RoleType preferredRole;
    private GameType preferredGame;
    private int skillLevel;                // 1–10

    public Participant() {
        // no-arg constructor required for CSV / frameworks
    }

    public Participant(
            String id,
            String name,
            String email,
            PersonalityType personalityType,
            int personalityScore,
            RoleType preferredRole,
            GameType preferredGame,
            int skillLevel
    ) {
        super(id, name, email);
        this.personalityType = personalityType;
        this.personalityScore = personalityScore;
        this.preferredRole = preferredRole;
        this.preferredGame = preferredGame;
        this.skillLevel = skillLevel;
    }

    // --- Getters & Setters ---

    public PersonalityType getPersonalityType() {
        return personalityType;
    }

    public void setPersonalityType(PersonalityType personalityType) {
        this.personalityType = personalityType;
    }

    public int getPersonalityScore() {
        return personalityScore;
    }

    public void setPersonalityScore(int personalityScore) {
        this.personalityScore = personalityScore;
    }

    public RoleType getPreferredRole() {
        return preferredRole;
    }

    public void setPreferredRole(RoleType preferredRole) {
        this.preferredRole = preferredRole;
    }

    public GameType getPreferredGame() {
        return preferredGame;
    }

    public void setPreferredGame(GameType preferredGame) {
        this.preferredGame = preferredGame;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

    // --- Utility methods ---

    @Override
    public String toString() {
        return "Participant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", personalityType=" + personalityType +
                ", personalityScore=" + personalityScore +
                ", preferredRole=" + preferredRole +
                ", preferredGame=" + preferredGame +
                ", skillLevel=" + skillLevel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant that)) return false;
        return Objects.equals(id, that.id);   // consider participants equal by id
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
