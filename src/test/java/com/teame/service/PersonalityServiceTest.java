package com.teame.service;

import com.teame.model.enums.PersonalityType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonalityServiceTest {

    private final PersonalityService service = new PersonalityService();

    @Test
    void calculateScore_sumsAllFiveAnswers() {
        int score = service.calculateScore(3, 4, 2, 5, 1);
        assertEquals(15, score);
    }

    @Test
    void scaleToHundred_multipliesByFour() {
        assertEquals(80, service.scaleToHundred(20));
        assertEquals(20, service.scaleToHundred(5));
    }

    @Test
    void classifyScore_leaderForHighScores() {
        assertEquals(PersonalityType.LEADER, service.classifyScore(90));
        assertEquals(PersonalityType.LEADER, service.classifyScore(100));
    }

    @Test
    void classifyScore_balancedFor70to89() {
        assertEquals(PersonalityType.BALANCED, service.classifyScore(70));
        assertEquals(PersonalityType.BALANCED, service.classifyScore(85));
    }

    @Test
    void classifyScore_thinkerFor50to69() {
        assertEquals(PersonalityType.THINKER, service.classifyScore(50));
        assertEquals(PersonalityType.THINKER, service.classifyScore(60));
    }

    @Test
    void classifyScore_needsReviewBelow50() {
        assertEquals(PersonalityType.NEEDS_REVIEW, service.classifyScore(0));
        assertEquals(PersonalityType.NEEDS_REVIEW, service.classifyScore(49));
    }
}
