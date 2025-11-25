package com.teame.repository;

import com.teame.model.Participant;

import java.util.List;

/**
 * Abstraction for loading and saving participant data from/to a persistence source (CSV, DB, etc.).
 * For this coursework, we'll implement it as a CSV-based repository.
 */
public interface ParticipantRepository {

    /**
     * Load all participants from the given file path.
     * Returns both valid participants and invalid rows.
     */
    ParticipantLoadResult loadAll(String filePath);

    /**
     * Save the given list of participants to the given file path.
     * Overwrites the file if it already exists.
     */
    void saveAll(String filePath, List<Participant> participants);

    /**
     * Append a single participant as a new row to the given file path.
     * Optional, but useful when a new participant completes the survey.
     */
    void append(String filePath, Participant participant);
}
