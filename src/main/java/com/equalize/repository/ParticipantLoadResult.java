package com.equalize.repository;

import com.equalize.model.Participant;
import com.equalize.model.dto.InvalidRow;

import java.util.List;

/**
 * Wrapper object returned when loading participants from CSV.
 * Contains both valid participants and invalid rows detected during validation.
 */
public class ParticipantLoadResult {

    private final List<Participant> validParticipants;
    private final List<InvalidRow> invalidRows;

    public ParticipantLoadResult(List<Participant> validParticipants, List<InvalidRow> invalidRows) {
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
