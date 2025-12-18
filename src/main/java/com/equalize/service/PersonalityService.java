package com.equalize.service;

import com.equalize.model.enums.PersonalityType;

/**
 * Handles personality score calculation and classification
 * based on the 5-question survey.
 */
public class PersonalityService {

    /**
     * Calculates the raw score (sum of answers).
     * Each question is expected to be in the range 1–5.
     */
    public int calculateScore(int q1, int q2, int q3, int q4, int q5) {
        return q1 + q2 + q3 + q4 + q5;
    }

    /**
     * Scales the raw score (5–25) to a 0–100 range.
     * According to the brief, we multiply by 4.
     */
    public int scaleToHundred(int rawScore) {
        return rawScore * 4;
    }

    /**
     * Classifies a scaled score (0–100) into a PersonalityType.
     *  - 90–100 : LEADER
     *  - 70–89  : BALANCED
     *  - 50–69  : THINKER
     *  - <50    : NEEDS_REVIEW
     */
    public PersonalityType classifyScore(int scaledScore) {
        if (scaledScore >= 90) {
            return PersonalityType.LEADER;
        } else if (scaledScore >= 70) {
            return PersonalityType.BALANCED;
        } else if (scaledScore >= 50) {
            return PersonalityType.THINKER;
        } else {
            return PersonalityType.NEEDS_REVIEW;
        }
    }

    /**
     * Convenience method: directly classify from the 5 answers.
     */
    public PersonalityType classifyFromAnswers(int q1, int q2, int q3, int q4, int q5) {
        int rawScore = calculateScore(q1, q2, q3, q4, q5);
        int scaled = scaleToHundred(rawScore);
        return classifyScore(scaled);
    }



}
