package com.teame.repository;

import com.teame.model.Team;

import java.util.List;

public interface TeamRepository {

    /**
     * Save all formed teams into a CSV file.
     * Overwrites the file if it already exists.
     */
    void saveAll(String filePath, List<Team> teams);
}
