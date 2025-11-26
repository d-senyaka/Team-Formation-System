package com.teame.concurrency;

import com.teame.model.Participant;
import com.teame.model.dto.InvalidRow;

import java.util.List;

/**
 * Overall result of running the concurrency pipeline:
 *  - all cleaned/valid participants
 *  - all invalid rows detected during processing
 */
public class ParticipantProcessingResult {

    private final List<Participant> validParticipants;
    private final List<InvalidRow> invalidRows;

    public ParticipantProcessingResult(List<Participant> validParticipants,
                                       List<InvalidRow> invalidRows) {
        this.validParticipants = validParticipants;
        this.invalidRows = invalidRows;
    }

    public List<Participant> getValidParticipants() {
        return validParticipants;
    }

    public List<InvalidRow> getInvalidRows() {
        return invalidRows;
    }
}
