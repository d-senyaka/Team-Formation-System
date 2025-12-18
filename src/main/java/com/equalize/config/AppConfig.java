package com.equalize.config;

import com.equalize.repository.CsvParticipantRepository;
import com.equalize.repository.ParticipantRepository;
import com.equalize.service.ParticipantService;
import com.equalize.service.ParticipantSession;
import com.equalize.service.PersonalityService;
import com.equalize.service.ValidationService;

import com.equalize.repository.CsvTeamRepository;
import com.equalize.repository.TeamRepository;
import com.equalize.concurrency.ConcurrencyService;
import com.equalize.service.TeamFormationService;
import com.equalize.service.OrganizerContext;


public class AppConfig {

    private static final String PARTICIPANTS_FILE =
            "src/main/resources/com/equalize/data/participants_saved.csv";

    private static final String FORMED_TEAMS_FILE =
            "src/main/resources/com/equalize/data/formed_teams.csv";

    private final ParticipantSession participantSession = new ParticipantSession();
    private final ValidationService validationService = new ValidationService();
    private final PersonalityService personalityService = new PersonalityService();
    private final CsvParticipantRepository participantRepository =
            new CsvParticipantRepository(validationService);
    private final ParticipantService participantService =
            new ParticipantService(participantRepository, PARTICIPANTS_FILE);


    // ---- New for organizer side ----
    private final OrganizerContext organizerContext = new OrganizerContext();
    private final ConcurrencyService concurrencyService =
            new ConcurrencyService(personalityService, validationService);
    private final TeamFormationService teamFormationService = new TeamFormationService();
    private final TeamRepository teamRepository =
            new CsvTeamRepository(FORMED_TEAMS_FILE);

    public ParticipantSession getParticipantSession() {
        return participantSession;
    }

    public ValidationService getValidationService() {
        return validationService;
    }

    public PersonalityService getPersonalityService() {
        return personalityService;
    }

    public ParticipantService getParticipantService() {
        return participantService;
    }


    // Organizer side getters
    public OrganizerContext getOrganizerContext() {
        return organizerContext;
    }

    public ConcurrencyService getConcurrencyService() {
        return concurrencyService;
    }

    public TeamFormationService getTeamFormationService() {
        return teamFormationService;
    }

    public TeamRepository getTeamRepository() {
        return teamRepository;
    }

    public ParticipantRepository getParticipantRepository() {
        return participantRepository;
    }

    public String getParticipantsFilePath() {
        return PARTICIPANTS_FILE;
    }




}
