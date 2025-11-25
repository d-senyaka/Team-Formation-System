package com.teame.model.enums;

/**
 * Personality type classified from the 5-question survey.
 * Score ranges (from the coursework docs):
 *  - 90–100 : LEADER
 *  - 70–89  : BALANCED
 *  - 50–69  : THINKER
 *  - <50    : NEEDS_REVIEW (optional flag)
 */
public enum PersonalityType {
    LEADER,
    BALANCED,
    THINKER,
    NEEDS_REVIEW
}
