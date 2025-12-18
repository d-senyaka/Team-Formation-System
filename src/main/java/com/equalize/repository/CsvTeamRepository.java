package com.equalize.repository;

import com.equalize.model.Participant;
import com.equalize.model.Team;
import com.equalize.model.enums.GameType;
import com.equalize.model.enums.PersonalityType;
import com.equalize.model.enums.RoleType;
import com.equalize.util.CsvUtils;

import java.util.ArrayList;
import java.util.List;

public class CsvTeamRepository implements TeamRepository {

    private final String filePath;

    public CsvTeamRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void saveAll(List<Team> teams) {
        List<String> lines = new ArrayList<>();

        // CSV header
        lines.add("TeamID,ParticipantID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType");

        for (Team team : teams) {
            for (Participant p : team.getMembers()) {
                String row = String.join(",",
                        safe(team.getTeamId()),
                        safe(p.getId()),
                        safe(p.getName()),
                        safe(p.getEmail()),
                        gameToCsv(p.getPreferredGame()),
                        String.valueOf(p.getSkillLevel()),
                        roleToCsv(p.getPreferredRole()),
                        String.valueOf(p.getPersonalityScore()),
                        personalityTypeToCsv(p.getPersonalityType())
                );
                lines.add(row);
            }
        }

        CsvUtils.writeAllLines(filePath, lines);
    }

    // --- Helper methods ---

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private String gameToCsv(GameType game) {
        if (game == null) return "";
        return switch (game) {
            case CHESS -> "Chess";
            case FIFA -> "FIFA";
            case DOTA2 -> "DOTA 2";
            case VALORANT -> "Valorant";
            case CSGO -> "CS:GO";
            case BASKETBALL -> "Basketball";
            case BADMINTON -> "Badminton";
        };
    }

    private String roleToCsv(RoleType role) {
        if (role == null) return "";
        String raw = role.name();
        return raw.charAt(0) + raw.substring(1).toLowerCase(); // STRATEGIST → Strategist
    }

    private String personalityTypeToCsv(PersonalityType type) {
        if (type == null) return "";
        return switch (type) {
            case LEADER -> "Leader";
            case BALANCED -> "Balanced";
            case THINKER -> "Thinker";
            case NEEDS_REVIEW -> "Needs Review";
        };
    }
}
