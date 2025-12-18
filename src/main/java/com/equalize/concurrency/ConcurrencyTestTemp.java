package com.equalize.concurrency;

import com.equalize.model.Participant;
import com.equalize.model.dto.InvalidRow;
import com.equalize.repository.CsvParticipantRepository;
import com.equalize.repository.ParticipantLoadResult;
import com.equalize.service.PersonalityService;
import com.equalize.service.ValidationService;
import com.equalize.util.CsvUtils;

import java.util.List;

public class ConcurrencyTestTemp {
    public static void main(String[] args) {

        String filePath = "src/main/resources/com/equalize/sample-data/participants_sample.csv";

        ValidationService validationService = new ValidationService();
        PersonalityService personalityService = new PersonalityService();

        CsvParticipantRepository csvRepo = new CsvParticipantRepository(validationService);

        // Load raw participants (already parsed by CsvParticipantRepository)
        ParticipantLoadResult csvResult = csvRepo.loadAll(filePath);

        List<Participant> rawParticipants = csvResult.getValidParticipants();
        List<String> rawLines = CsvUtils.readAllLines(filePath);

        ConcurrencyService concurrencyService = new ConcurrencyService(personalityService, validationService);

        ParticipantLoadResult processed = concurrencyService.processParticipants(rawParticipants, rawLines);

        System.out.println("=== Concurrency TEST RESULT ===");
        System.out.println("Valid participants after concurrency: " + processed.getValidParticipants().size());
        System.out.println("Invalid rows after concurrency: " + processed.getInvalidRows().size());
        System.out.println();

        System.out.println("--- First few processed participants ---");
        processed.getValidParticipants().stream()
                .limit(5)
                .forEach(System.out::println);

        System.out.println();
        System.out.println("--- Invalid rows during concurrency ---");
        for (InvalidRow row : processed.getInvalidRows()) {
            System.out.println(row);
        }

        concurrencyService.shutdown();
    }
}
