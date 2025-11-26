package com.teame.config;

import com.teame.repository.CsvParticipantRepository;
import com.teame.repository.ParticipantRepository;
import com.teame.service.ParticipantService;
import com.teame.service.ParticipantSession;
import com.teame.service.PersonalityService;
import com.teame.service.ValidationService;

import com.teame.repository.CsvTeamRepository;
import com.teame.repository.TeamRepository;
import com.teame.concurrency.ConcurrencyService;
import com.teame.service.TeamFormationService;
import com.teame.service.OrganizerContext;


public class AppConfig {

    private static final String PARTICIPANTS_FILE =
            "src/main/resources/com/teame/data/participants_saved.csv";

    private static final String FORMED_TEAMS_FILE =
            "src/main/resources/com/teame/data/formed_teams.csv";

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
