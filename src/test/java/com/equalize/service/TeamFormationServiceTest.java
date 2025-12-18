package com.equalize.service;

import com.equalize.model.Participant;
import com.equalize.model.Team;
import com.equalize.util.TestParticipantsFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeamFormationServiceTest {

    @Test
    void formTeams_producesTeamsWithExpectedSize() {
        TeamFormationService service = new TeamFormationService();

        // build 6 participants for 2 teams of 3
        List<Participant> participants = TestParticipantsFactory.createSampleParticipants(6);

        List<Team> teams = service.formBalancedTeams(participants, 3);
        assertEquals(2, teams.size());
        assertTrue(teams.stream().allMatch(t -> t.getMembers().size() == 3));
    }
}
