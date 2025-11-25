package com.teame.repository;

import com.teame.model.Participant;
import com.teame.model.dto.InvalidRow;
import com.teame.service.ValidationService;

import java.util.List;

public class CsvTestTemp {

    public static void main(String[] args) {
        // Adjust this path if needed.
        // If you run from project root, this relative path usually works:
        String filePath = "src/main/resources/com/teame/sample-data/participants_sample.csv";

        ValidationService validationService = new ValidationService();
        CsvParticipantRepository repo = new CsvParticipantRepository(validationService);

        ParticipantLoadResult result = repo.loadAll(filePath);

        List<Participant> valid = result.getValidParticipants();
        List<InvalidRow> invalid = result.getInvalidRows();

        System.out.println("=== CSV LOAD RESULT ===");
        System.out.println("Valid participants: " + valid.size());
        System.out.println("Invalid rows: " + invalid.size());
        System.out.println();

        System.out.println("--- First few valid participants ---");
        valid.stream()
                .limit(5)
                .forEach(System.out::println);

        System.out.println();
        System.out.println("--- Invalid rows (if any) ---");
        invalid.forEach(System.out::println);
    }
}
