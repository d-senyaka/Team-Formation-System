package com.equalize.service;

import com.equalize.model.Participant;
import com.equalize.model.Team;
import com.equalize.model.dto.InvalidRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared state for organizer-side operations.
 * Holds the currently loaded/cleaned participants, invalid rows, and formed teams.
 */
public class OrganizerContext {

    private String sourceFilePath;

    private final List<Participant> cleanedParticipants = new ArrayList<>();
    private final List<InvalidRow> invalidRows = new ArrayList<>();
    private final List<Team> formedTeams = new ArrayList<>();

    public String getSourceFilePath() {
        return sourceFilePath;
    }

    public void setSourceFilePath(String sourceFilePath) {
        this.sourceFilePath = sourceFilePath;
    }

    // ---- Cleaned participants ----
    public List<Participant> getCleanedParticipants() {
        return cleanedParticipants;
    }

    public void setCleanedParticipants(List<Participant> participants) {
        cleanedParticipants.clear();
        if (participants != null) {
            cleanedParticipants.addAll(participants);
        }
    }

    // ---- Invalid rows ----
    public List<InvalidRow> getInvalidRows() {
        return invalidRows;
    }

    public void setInvalidRows(List<InvalidRow> rows) {
        invalidRows.clear();
        if (rows != null) {
            invalidRows.addAll(rows);
        }
    }

    // ---- Formed teams ----
    public List<Team> getFormedTeams() {
        return formedTeams;
    }

    public void setFormedTeams(List<Team> teams) {
        formedTeams.clear();
        if (teams != null) {
            formedTeams.addAll(teams);
        }
    }

    public void clearAll() {
        sourceFilePath = null;
        cleanedParticipants.clear();
        invalidRows.clear();
        formedTeams.clear();
    }
}
