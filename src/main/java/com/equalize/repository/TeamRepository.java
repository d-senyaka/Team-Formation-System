package com.equalize.repository;

import com.equalize.model.Team;

import java.util.List;

public interface TeamRepository {
    void saveAll(List<Team> teams);
}

