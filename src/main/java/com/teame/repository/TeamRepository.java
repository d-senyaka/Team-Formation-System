package com.teame.repository;

import com.teame.model.Team;

import java.util.List;

public interface TeamRepository {
    void saveAll(List<Team> teams);
}

