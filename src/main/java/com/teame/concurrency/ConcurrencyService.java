package com.teame.concurrency;

import com.teame.model.Participant;
import com.teame.model.dto.InvalidRow;
import com.teame.repository.ParticipantLoadResult;
import com.teame.service.PersonalityService;
import com.teame.service.ValidationService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.Collections;

/**
 * Handles parallel processing of participant records using a thread pool.
 */
public class ConcurrencyService {

    private final ExecutorService executor;
    private final PersonalityService personalityService;
    private final ValidationService validationService;

    public ConcurrencyService(PersonalityService personalityService, ValidationService validationService) {
        this.executor = Executors.newFixedThreadPool(5); // 5 threads (can adjust later)
        this.personalityService = personalityService;
        this.validationService = validationService;
    }

    /**
     * Process a list of participants in parallel.
     *
     * @param participants list of participants from CSV (already parsed)
     * @return ParticipantLoadResult containing cleaned participants and invalid rows
     */
    public ParticipantLoadResult processParticipants(List<Participant> participants, List<String> rawLines) {

        List<Callable<ProcessedResult>> tasks = new ArrayList<>();

        for (int i = 0; i < participants.size(); i++) {
            Participant p = participants.get(i);
            String rawLine = rawLines.get(i + 1); // +1 because line 0 = header

            tasks.add(new ParticipantProcessingTask(
                    p,
                    i + 2,             // true row number in CSV (header = row 1)
                    rawLine,
                    personalityService,
                    validationService
            ));
        }

        List<Participant> validList = new ArrayList<>();
        List<InvalidRow> invalidList = new ArrayList<>();

        try {
            List<Future<ProcessedResult>> futures = executor.invokeAll(tasks);

            for (Future<ProcessedResult> future : futures) {
                try {
                    ProcessedResult result = future.get();

                    if (result.isValid()) {
                        validList.add(result.getParticipant());
                    } else {
                        invalidList.add(result.getInvalidRow());
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Concurrency interrupted: " + e.getMessage());
        }

        return new ParticipantLoadResult(validList, invalidList);
    }

    /**
     * Shut down the thread pool gracefully.
     */
    public void shutdown() {
        executor.shutdown();
    }
}
