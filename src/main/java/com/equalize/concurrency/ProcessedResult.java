package com.equalize.concurrency;

import com.equalize.model.Participant;
import com.equalize.model.dto.InvalidRow;

/**
 * Wraps the result of a single participant task.
 * Either "participant" OR "invalidRow" is non-null.
 */
public class ProcessedResult {

    private Participant participant;
    private InvalidRow invalidRow;

    public ProcessedResult(Participant participant) {
        this.participant = participant;
    }

    public ProcessedResult(InvalidRow invalidRow) {
        this.invalidRow = invalidRow;
    }

    public Participant getParticipant() {
        return participant;
    }

    public InvalidRow getInvalidRow() {
        return invalidRow;
    }

    public boolean isValid() {
        return participant != null;
    }
}
