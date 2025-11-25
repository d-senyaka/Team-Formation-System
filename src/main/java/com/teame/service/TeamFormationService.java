package com.teame.service;

import com.teame.model.Participant;
import com.teame.model.enums.GameType;
import com.teame.model.enums.PersonalityType;
import com.teame.model.enums.RoleType;

import java.util.*;

/**
 * Handles the full team formation pipeline.
 * Step 1: Bucket Creation - grouping participants by major characteristics.
 */
public class TeamFormationService {

    /**
     * Bucket participants by their preferred role.
     */
    public Map<RoleType, List<Participant>> bucketByRole(List<Participant> participants) {
        Map<RoleType, List<Participant>> map = new EnumMap<>(RoleType.class);

        for (RoleType role : RoleType.values()) {
            map.put(role, new ArrayList<>());
        }

        for (Participant p : participants) {
            if (p.getPreferredRole() != null) {
                map.get(p.getPreferredRole()).add(p);
            }
        }

        return map;
    }

    /**
     * Bucket participants by preferred game.
     */
    public Map<GameType, List<Participant>> bucketByGame(List<Participant> participants) {
        Map<GameType, List<Participant>> map = new EnumMap<>(GameType.class);

        for (GameType game : GameType.values()) {
            map.put(game, new ArrayList<>());
        }

        for (Participant p : participants) {
            if (p.getPreferredGame() != null) {
                map.get(p.getPreferredGame()).add(p);
            }
        }

        return map;
    }

    /**
     * Bucket participants by personality type.
     */
    public Map<PersonalityType, List<Participant>> bucketByPersonality(List<Participant> participants) {
        Map<PersonalityType, List<Participant>> map = new EnumMap<>(PersonalityType.class);

        for (PersonalityType type : PersonalityType.values()) {
            map.put(type, new ArrayList<>());
        }

        for (Participant p : participants) {
            if (p.getPersonalityType() != null) {
                map.get(p.getPersonalityType()).add(p);
            }
        }

        return map;
    }
}

