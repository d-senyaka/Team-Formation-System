package com.equalize.concurrency;

import com.equalize.model.Participant;
import com.equalize.model.dto.InvalidRow;
import com.equalize.service.PersonalityService;
import com.equalize.service.ValidationService;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Task to preprocess a participant:
 * - Normalize fields
 * - Recompute personality if needed
 * - Validate
 */
public class ParticipantProcessingTask implements Callable<ProcessedResult> {

    private final Participant participant;
    private final int rowNumber;
    private final String rawLine;

    private final PersonalityService personalityService;
    private final ValidationService validationService;

    public ParticipantProcessingTask(
            Participant participant,
            int rowNumber,
            String rawLine,
            PersonalityService personalityService,
            ValidationService validationService) {

        this.participant = participant;
        this.rowNumber = rowNumber;
        this.rawLine = rawLine;
        this.personalityService = personalityService;
        this.validationService = validationService;
    }

    @Override
    public ProcessedResult call() {

        // ---- NORMALIZATION STEP ----
        if (participant.getName() != null)
            participant.setName(participant.getName().trim());

        if (participant.getEmail() != null)
            participant.setEmail(participant.getEmail().trim().toLowerCase());

        // If personality score is missing or invalid → recompute
        if (participant.getPersonalityScore() < 0 || participant.getPersonalityScore() > 100) {
            // (Using raw answers is optional; your CSV has the score already)
        }

        // ---- VALIDATION ----
        List<String> errors = validationService.validateParticipant(participant);

        if (!errors.isEmpty()) {
            return new ProcessedResult(
                    new InvalidRow(rowNumber, rawLine, errors)
            );
        }

        return new ProcessedResult(participant);
    }
}
