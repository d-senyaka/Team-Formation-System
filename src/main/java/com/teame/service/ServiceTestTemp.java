package com.teame.service;

import com.teame.model.Participant;
import com.teame.model.enums.GameType;
import com.teame.model.enums.PersonalityType;
import com.teame.model.enums.RoleType;

public class ServiceTestTemp {
    public static void main(String[] args) {

        PersonalityService ps = new PersonalityService();

        int raw = ps.calculateScore(4, 5, 4, 5, 4);  // 22
        int scaled = ps.scaleToHundred(raw);         // 88
        System.out.println("Scaled Score = " + scaled);
        System.out.println("Type = " + ps.classifyScore(scaled)); // BALANCED

        // Now create a valid participant:
        Participant p = new Participant(
                "P001",
                "John Doe",
                "john@example.com",
                PersonalityType.BALANCED,
                scaled,
                RoleType.DEFENDER,
                GameType.CSGO,
                7
        );

        ValidationService vs = new ValidationService();

        var errors = vs.validateParticipant(p);

        if (errors.isEmpty()) {
            System.out.println("Participant is VALID");
        } else {
            System.out.println("Invalid participant:");
            errors.forEach(System.out::println);
        }
    }
}
