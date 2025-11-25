package com.teame.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a formed team with a unique ID and a list of participants.
 * Additional metrics (average skill, dominant game) can be added later.
 */
public class Team {

    private String teamId;                    // e.g., "Team 1", "Alpha", "Group A"
    private List<Participant> members;        // dynamic list of participants

    public Team(String teamId) {
        this.teamId = teamId;
        this.members = new ArrayList<>();
    }

    public Team(String teamId, List<Participant> members) {
        this.teamId = teamId;
        this.members = members;
    }

    // --- Getters & Setters ---

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public List<Participant> getMembers() {
        return members;
    }

    public void setMembers(List<Participant> members) {
        this.members = members;
    }

    // --- Convenience methods ---

    public void addMember(Participant p) {
        this.members.add(p);
    }

    public int size() {
        return members.size();
    }

    public double getAverageSkill() {
        if (members.isEmpty()) return 0;

        return members.stream()
                .mapToInt(Participant::getSkillLevel)
                .average()
                .orElse(0);
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamId='" + teamId + '\'' +
                ", members=" + members +
                '}';
    }
}
