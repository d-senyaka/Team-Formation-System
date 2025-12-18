package com.equalize.service;

import com.equalize.exception.ValidationException;
import com.equalize.model.Participant;
import com.equalize.model.enums.GameType;
import com.equalize.model.enums.PersonalityType;
import com.equalize.model.enums.RoleType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Performs validation on Participant objects and related fields.
 */
public class ValidationService {

    // Simple email pattern, not perfect but good enough for coursework
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    /**
     * Validate the given participant and return a list of error messages.
     * If the list is empty, the participant is considered valid.
     */
    public List<String> validateParticipant(Participant p) {
        List<String> errors = new ArrayList<>();

        if (p == null) {
            errors.add("Participant is null.");
            return errors;
        }

        // ID
        if (isNullOrBlank(p.getId())) {
            errors.add("Participant ID is required.");
        }

        // Name
        if (isNullOrBlank(p.getName())) {
            errors.add("Participant name is required.");
        }

        // Email
        if (isNullOrBlank(p.getEmail())) {
            errors.add("Email is required.");
        } else if (!isValidEmail(p.getEmail())) {
            errors.add("Email format is invalid.");
        }

        // Preferred Game
        GameType game = p.getPreferredGame();
        if (game == null) {
            errors.add("Preferred game must be selected.");
        }

        // Preferred Role
        RoleType role = p.getPreferredRole();
        if (role == null) {
            errors.add("Preferred role must be selected.");
        }

        // Skill level (1–10)
        int skill = p.getSkillLevel();
        if (skill < 1 || skill > 10) {
            errors.add("Skill level must be between 1 and 10.");
        }

        // Personality score (0–100) – this will come from PersonalityService
        int score = p.getPersonalityScore();
        if (score < 0 || score > 100) {
            errors.add("Personality score must be between 0 and 100.");
        }

        // Personality type (non-null, auto-align with score ranges)
        PersonalityType type = p.getPersonalityType();
        if (type == null) {
            errors.add("Personality type is required.");
        } else {
            // Compute the expected type based on coursework spec
            PersonalityType expected;
            if (score >= 90) {
                expected = PersonalityType.LEADER;      // 90–100
            } else if (score >= 70) {
                expected = PersonalityType.BALANCED;    // 70–89
            } else if (score >= 50) {
                expected = PersonalityType.THINKER;     // 50–69
            } else {
                // < 50 — extension beyond brief, your own category
                expected = PersonalityType.NEEDS_REVIEW;
            }

            // If mismatched, auto-correct instead of throwing an error
            if (type != expected) {
                p.setPersonalityType(expected);
            }
        }


        return errors;
    }

    /**
     * Validate the participant and throw ValidationException if there are any errors.
     */
    public void validateParticipantOrThrow(Participant p) {
        List<String> errors = validateParticipant(p);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    // --- Helper methods ---

    private boolean isNullOrBlank(String s) {
        return s == null || s.isBlank();
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
