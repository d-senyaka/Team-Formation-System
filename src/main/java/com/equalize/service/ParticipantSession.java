package com.equalize.service;

import com.equalize.model.Participant;

/**
 * Simple holder for the currently logged-in participant.
 * This can be injected or passed to controllers that need it.
 */
public class ParticipantSession {

    private Participant currentParticipant;

    public Participant getCurrentParticipant() {
        return currentParticipant;
    }

    public void setCurrentParticipant(Participant currentParticipant) {
        this.currentParticipant = currentParticipant;
    }

    public boolean isLoggedIn() {
        return currentParticipant != null;
    }

    public void clear() {
        this.currentParticipant = null;
    }
}
