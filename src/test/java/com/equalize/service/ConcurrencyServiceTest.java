package com.equalize.service;

import com.equalize.model.Participant;
import com.equalize.model.enums.GameType;
import com.equalize.model.enums.PersonalityType;
import com.equalize.concurrency.ConcurrencyService;
import com.equalize.model.enums.RoleType;
import com.equalize.repository.ParticipantLoadResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrencyServiceTest {

    @Test
    void processParticipants_returnsSameCountAsInput() {
        ValidationService validationService = new ValidationService();
        PersonalityService personalityService = new PersonalityService();
        ConcurrencyService concurrencyService =
                new ConcurrencyService(personalityService, validationService);

        List<Participant> participants = List.of(
                buildParticipant("P001"),
                buildParticipant("P002")
        );

        List<String> rawLines = List.of(
                "P001,Alice,user1@university.edu,FIFA,4,ATTACKER,60,THINKER",
                "P002,Bob,user2@university.edu,CSGO,6,COORDINATOR,64,THINKER"
        );

        ParticipantLoadResult result = concurrencyService.processParticipants(participants, rawLines);

        assertEquals(2, result.getValidParticipants().size());
        assertEquals(0, result.getInvalidRows().size());
    }

    private Participant buildParticipant(String id) {
        Participant p = new Participant();
        p.setId(id);
        p.setName(id);
        p.setEmail(id.toLowerCase() + "@university.edu");
        p.setSkillLevel(5);
        p.setPreferredRole(RoleType.ATTACKER);
        p.setPreferredGame(GameType.FIFA);
        p.setPersonalityScore(80);
        p.setPersonalityType(PersonalityType.BALANCED);
        return p;
    }
}
