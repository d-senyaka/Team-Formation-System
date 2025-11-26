package com.teame.util;

import com.teame.model.Participant;
import com.teame.model.enums.GameType;
import com.teame.model.enums.PersonalityType;
import com.teame.model.enums.RoleType;

import java.util.ArrayList;
import java.util.List;

public class TestParticipantsFactory {
    
    public static List<Participant> createSampleParticipants(int count) {
        List<Participant> participants = new ArrayList<>();
        
        // Define some sample data for variety
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
}
