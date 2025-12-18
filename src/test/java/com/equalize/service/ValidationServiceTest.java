package com.equalize.service;

import com.equalize.model.Participant;
import com.equalize.model.enums.GameType;
import com.equalize.model.enums.PersonalityType;
import com.equalize.model.enums.RoleType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceTest {

    private final ValidationService validationService = new ValidationService();

    private Participant buildValidParticipant() {
        Participant p = new Participant();
        p.setId("P001");
        p.setName("Alice");
        p.setEmail("user@university.edu");
        p.setPreferredGame(GameType.FIFA);
        p.setPreferredRole(RoleType.ATTACKER);
        p.setSkillLevel(5);
        p.setPersonalityScore(80);
        p.setPersonalityType(PersonalityType.BALANCED);
        return p;
    }

    @Test
    void validateParticipant_validParticipant_hasNoErrors() {
        Participant p = buildValidParticipant();
        List<String> errors = validationService.validateParticipant(p);
        assertTrue(errors.isEmpty(), "Expected no validation errors for a valid participant");
    }

    @Test
    void validateParticipant_invalidEmail_reportsError() {
        Participant p = buildValidParticipant();
        p.setEmail("not-an-email");

        List<String> errors = validationService.validateParticipant(p);

        assertTrue(errors.stream().anyMatch(e -> e.contains("Email format is invalid")),
                "Expected invalid email error");
    }

    @Test
    void validateParticipant_outOfRangeSkill_reportsError() {
        Participant p = buildValidParticipant();
        p.setSkillLevel(11);

        List<String> errors = validationService.validateParticipant(p);

        assertTrue(errors.stream().anyMatch(e -> e.contains("Skill level must be between 1 and 10")),
                "Expected skill level range error");
    }

    @Test
    void validateParticipant_outOfRangeScore_reportsError() {
        Participant p = buildValidParticipant();
        p.setPersonalityScore(120);

        List<String> errors = validationService.validateParticipant(p);

        assertTrue(errors.stream().anyMatch(e -> e.contains("Personality score must be between 0 and 100")),
                "Expected personality score range error");
    }

    @Test
    void validateParticipant_missingRoleAndGame_reportsErrors() {
        Participant p = buildValidParticipant();
        p.setPreferredGame(null);
        p.setPreferredRole(null);

        List<String> errors = validationService.validateParticipant(p);

        assertTrue(errors.stream().anyMatch(e -> e.contains("Preferred game must be selected")));
        assertTrue(errors.stream().anyMatch(e -> e.contains("Preferred role must be selected")));
    }
}
