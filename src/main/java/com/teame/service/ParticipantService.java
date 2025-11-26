package com.teame.service;

import com.teame.model.Participant;
import com.teame.repository.CsvParticipantRepository;
import com.teame.repository.ParticipantLoadResult;
import com.teame.repository.ParticipantRepository;

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
}
