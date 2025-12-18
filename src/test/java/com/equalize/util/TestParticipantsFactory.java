package com.equalize.util;

import com.equalize.model.Participant;
import com.equalize.model.enums.GameType;
import com.equalize.model.enums.PersonalityType;
import com.equalize.model.enums.RoleType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test class for verifying createSampleParticipants()
 * Does NOT interfere with other tests.
 */
public class TestParticipantsFactory{

    /**
     * The same sample participant generator you had.
     * Other test classes can safely call this.
     */
    public static List<Participant> createSampleParticipants(int count) {
        List<Participant> participants = new ArrayList<>();

        String[] firstNames = {"Alex", "Jordan", "Taylor", "Casey", "Riley", "Jamie", "Quinn", "Avery", "Morgan", "Skyler"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Miller", "Davis", "Garcia", "Rodriguez", "Wilson"};
        RoleType[] roles = RoleType.values();
        PersonalityType[] personalities = PersonalityType.values();
        GameType[] gameTypes = GameType.values();

        for (int i = 0; i < count; i++) {
            String firstName = firstNames[i % firstNames.length];
            String lastName = lastNames[i % lastNames.length];
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com";

            Participant participant = new Participant();
            participant.setId(String.valueOf(i + 1));
            participant.setName(firstName + " " + lastName);
            participant.setEmail(email);
            participant.setPreferredRole(roles[i % roles.length]);
            participant.setPersonalityType(personalities[i % personalities.length]);
            participant.setPreferredGame(gameTypes[i % gameTypes.length]);

            participants.add(participant);
        }

        return participants;
    }

    // ------------------------------
    // Actual tests start here
    // ------------------------------

    @Test
    void testCreatesCorrectNumberOfParticipants() {
        List<Participant> list = createSampleParticipants(10);
        assertEquals(10, list.size());
    }

    @Test
    void testEachParticipantHasIdNameEmail() {
        List<Participant> list = createSampleParticipants(5);

        for (int i = 0; i < list.size(); i++) {
            Participant p = list.get(i);

            assertNotNull(p.getId());
            assertNotNull(p.getName());
            assertNotNull(p.getEmail());
            assertTrue(p.getEmail().contains("@example.com"));
        }
    }

    @Test
    void testRolesAreCycledCorrectly() {
        List<Participant> list = createSampleParticipants( RoleType.values().length * 2 );

        for (int i = 0; i < list.size(); i++) {
            assertEquals(RoleType.values()[i % RoleType.values().length],
                    list.get(i).getPreferredRole());
        }
    }
}
