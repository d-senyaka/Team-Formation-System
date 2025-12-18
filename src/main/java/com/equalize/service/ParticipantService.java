package com.equalize.service;

import com.equalize.exception.ValidationException;
import com.equalize.model.Participant;
import com.equalize.repository.ParticipantLoadResult;
import com.equalize.repository.ParticipantRepository;

import java.util.List;
import java.util.Optional;

public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final String participantsFilePath;

    public ParticipantService(ParticipantRepository participantRepository,
                              String participantsFilePath) {
        this.participantRepository = participantRepository;
        this.participantsFilePath = participantsFilePath;
    }

    /**
     * Load all participants from CSV.
     * In a real app you might cache this, but for coursework this is fine.
     */
    public List<Participant> loadAll() {
        ParticipantLoadResult result = participantRepository.loadAll(participantsFilePath);
        return result.getValidParticipants();
    }


    /**
     * Try to find an existing participant by id + email.
     */
    public Optional<Participant> findByIdAndEmail(String id, String email) {
        return loadAll().stream()
                .filter(p -> id.equalsIgnoreCase(p.getId())
                        && email.equalsIgnoreCase(p.getEmail()))
                .findFirst();
    }

    /**
     * Save all participants back to CSV.
     */
    public void saveAll(List<Participant> participants) {
        participantRepository.saveAll(participantsFilePath, participants);
    }

    /**
     * Either return existing participant or create a new one with given id+email.
     */
    public Participant findOrCreateParticipant(String id, String email) {
        Optional<Participant> existing = findByIdAndEmail(id, email);
        if (existing.isPresent()) {
            return existing.get();
        }

        Participant p = new Participant();
        p.setId(id);
        p.setEmail(email);
        p.setName(id); // temporary – later you might ask for name on survey/login

        List<Participant> all = loadAll();
        all.add(p);
        saveAll(all);

        return p;
    }

    public Participant createNewParticipant(String id, String email) {
        List<Participant> all = loadAll();

        // 1. Same ID + same email → already has an account
        for (Participant p : all) {
            if (p.getId().equalsIgnoreCase(id) &&
                    p.getEmail().equalsIgnoreCase(email)) {

                throw new ValidationException(
                        "An account already exists for this ID and email. " +
                                "Please use the Login button instead."
                );
            }
        }

        // 2. Same ID + different email → suspicious / typo
        for (Participant p : all) {
            if (p.getId().equalsIgnoreCase(id) &&
                    !p.getEmail().equalsIgnoreCase(email)) {

                throw new ValidationException(
                        "This participant ID is already used with another email. " +
                                "Please check your ID."
                );
            }
        }

        // 3. Different ID + same email → not allowed (one account per email)
        for (Participant p : all) {
            if (!p.getId().equalsIgnoreCase(id) &&
                    p.getEmail().equalsIgnoreCase(email)) {

                throw new ValidationException(
                        "This email address is already registered with another participant ID. " +
                                "Please check your ID or email."
                );
            }
        }

        // 4. All clear → create new participant
        Participant newP = new Participant();
        newP.setId(id);
        newP.setEmail(email);
        newP.setName(id); // or "" or ask later

        all.add(newP);
        saveAll(all);

        return newP;
    }


}
