package com.teame.config;

import com.teame.repository.CsvParticipantRepository;
import com.teame.service.ParticipantService;
import com.teame.service.ParticipantSession;
import com.teame.service.PersonalityService;
import com.teame.service.ValidationService;

public class AppConfig {

    private static final String PARTICIPANTS_FILE =
            "src/main/resources/com/teame/data/participants_saved.csv";

    private final ParticipantSession participantSession = new ParticipantSession();
    private final ValidationService validationService = new ValidationService();
    private final PersonalityService personalityService = new PersonalityService();
    private final CsvParticipantRepository participantRepository =
            new CsvParticipantRepository(validationService);
    private final ParticipantService participantService =
            new ParticipantService(participantRepository, PARTICIPANTS_FILE);

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
}
