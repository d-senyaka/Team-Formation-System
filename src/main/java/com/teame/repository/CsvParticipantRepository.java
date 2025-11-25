package com.teame.repository;

import com.teame.model.Participant;
import com.teame.model.dto.InvalidRow;
import com.teame.model.enums.GameType;
import com.teame.model.enums.PersonalityType;
import com.teame.model.enums.RoleType;
import com.teame.service.ValidationService;
import com.teame.util.CsvUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * CSV-based implementation of ParticipantRepository.
 * Reads/writes participants using a fixed column order:
 * ID, Name, Email, PreferredGame, SkillLevel,
 * PreferredRole, PersonalityScore, PersonalityType
 */
public class CsvParticipantRepository implements ParticipantRepository {

    private final ValidationService validationService;

    public CsvParticipantRepository(ValidationService validationService) {
        this.validationService = validationService;
    }

    @Override
    public ParticipantLoadResult loadAll(String filePath) {
        List<String> lines = CsvUtils.readAllLines(filePath);

        List<Participant> validParticipants = new ArrayList<>();
        List<InvalidRow> invalidRows = new ArrayList<>();

        if (lines.isEmpty()) {
            return new ParticipantLoadResult(validParticipants, invalidRows);
        }

        // first line is header, start from index 1
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line == null || line.isBlank()) {
                continue;
            }

            String[] parts = line.split(",", -1); // include empty strings
            List<String> parseErrors = new ArrayList<>();

            if (parts.length < 8) {
                parseErrors.add("Expected 8 columns, found " + parts.length);
                invalidRows.add(new InvalidRow(i + 1, line, parseErrors));
                continue;
            }

            String id = parts[0].trim();
            String name = parts[1].trim();
            String email = parts[2].trim();
            String gameStr = parts[3].trim();
            String skillStr = parts[4].trim();
            String roleStr = parts[5].trim();
            String scoreStr = parts[6].trim();
            String typeStr = parts[7].trim();

            // --- Parse enums and numbers with safety ---

            GameType game = parseGameType(gameStr, parseErrors);
            RoleType role = parseRoleType(roleStr, parseErrors);
            PersonalityType pType = parsePersonalityType(typeStr, parseErrors);

            int skillLevel = parseInt(skillStr, "SkillLevel", 1, 10, parseErrors);
            int personalityScore = parseInt(scoreStr, "PersonalityScore", 0, 100, parseErrors);

            // If parsing itself failed, no need to run validation
            if (!parseErrors.isEmpty()) {
                invalidRows.add(new InvalidRow(i + 1, line, parseErrors));
                continue;
            }

            Participant participant = new Participant(
                    id,
                    name,
                    email,
                    pType,
                    personalityScore,
                    role,
                    game,
                    skillLevel
            );

            // --- Business validation using ValidationService ---
            List<String> validationErrors = validationService.validateParticipant(participant);

            if (validationErrors.isEmpty()) {
                validParticipants.add(participant);
            } else {
                invalidRows.add(new InvalidRow(i + 1, line, validationErrors));
            }
        }

        return new ParticipantLoadResult(validParticipants, invalidRows);
    }

    @Override
    public void saveAll(String filePath, List<Participant> participants) {
        List<String> lines = new ArrayList<>();

        // header
        lines.add("ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType");

        for (Participant p : participants) {
            String row = String.join(",",
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

        CsvUtils.writeAllLines(filePath, lines);
    }

    @Override
    public void append(String filePath, Participant participant) {
        // We assume the file already has a header.
        // If you want, you can later enhance this to create header if file doesn't exist.
        String row = String.join(",",
                safe(participant.getId()),
                safe(participant.getName()),
                safe(participant.getEmail()),
                gameToCsv(participant.getPreferredGame()),
                String.valueOf(participant.getSkillLevel()),
                roleToCsv(participant.getPreferredRole()),
                String.valueOf(participant.getPersonalityScore()),
                personalityTypeToCsv(participant.getPersonalityType())
        );

        CsvUtils.appendLine(filePath, row);
    }

    // ----------------- Helper methods -----------------

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private int parseInt(String value, String fieldName, int min, int max, List<String> errors) {
        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed < min || parsed > max) {
                errors.add(fieldName + " must be between " + min + " and " + max + ". Found: " + parsed);
            }
            return parsed;
        } catch (NumberFormatException e) {
            errors.add(fieldName + " is not a valid integer: '" + value + "'");
            return 0;
        }
    }

    private GameType parseGameType(String value, List<String> errors) {
        if (value == null || value.isBlank()) {
            errors.add("PreferredGame is required.");
            return null;
        }

        String normalized = value.trim().toUpperCase()
                .replace(" ", "")
                .replace(":", "");

        switch (normalized) {
            case "CHESS":
                return GameType.CHESS;
            case "FIFA":
                return GameType.FIFA;
            case "DOTA2":
                return GameType.DOTA2;
            case "VALORANT":
                return GameType.VALORANT;
            case "CSGO":
                return GameType.CSGO;
            case "BASKETBALL":
                return GameType.BASKETBALL;
            case "BADMINTON":
                return GameType.BADMINTON;
            default:
                errors.add("Unknown game type: '" + value + "'");
                return null;
        }
    }

    private RoleType parseRoleType(String value, List<String> errors) {
        if (value == null || value.isBlank()) {
            errors.add("PreferredRole is required.");
            return null;
        }

        String normalized = value.trim().toUpperCase();
        try {
            return RoleType.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            errors.add("Unknown role type: '" + value + "'");
            return null;
        }
    }

    private PersonalityType parsePersonalityType(String value, List<String> errors) {
        if (value == null || value.isBlank()) {
            errors.add("PersonalityType is required.");
            return null;
        }

        // e.g. "Leader", "Balanced", "Thinker", "Needs Review"
        String normalized = value.trim().toUpperCase().replace(" ", "_");

        try {
            return PersonalityType.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            errors.add("Unknown personality type: '" + value + "'");
            return null;
        }
    }

    private String gameToCsv(GameType game) {
        if (game == null) return "";
        switch (game) {
            case CHESS:
                return "Chess";
            case FIFA:
                return "FIFA";
            case DOTA2:
                return "DOTA 2";
            case VALORANT:
                return "Valorant";
            case CSGO:
                return "CS:GO";
            case BASKETBALL:
                return "Basketball";
            case BADMINTON:
                return "Badminton";
            default:
                return game.name();
        }
    }

    private String roleToCsv(RoleType role) {
        if (role == null) return "";
        String name = role.name(); // e.g. STRATEGIST
        String lower = name.substring(1).toLowerCase();
        return name.charAt(0) + lower; // "Strategist"
    }

    private String personalityTypeToCsv(PersonalityType type) {
        if (type == null) return "";
        switch (type) {
            case LEADER:
                return "Leader";
            case BALANCED:
                return "Balanced";
            case THINKER:
                return "Thinker";
            case NEEDS_REVIEW:
                return "Needs Review";
            default:
                return type.name();
        }
    }
}
