package com.teame.service;

import com.teame.concurrency.ConcurrencyService;
import com.teame.model.Participant;
import com.teame.model.Team;
import com.teame.model.dto.InvalidRow;
import com.teame.repository.CsvParticipantRepository;
import com.teame.repository.ParticipantLoadResult;
import com.teame.util.CsvUtils;

import java.util.List;

public class TeamFormationPipelineTestTemp {

    public static void main(String[] args) {

        // 1) CSV path (adjust if your path is different)
        String filePath = "src/main/resources/com/teame/sample-data/participants_sample.csv";

        // 2) Core services
        ValidationService validationService = new ValidationService();
        PersonalityService personalityService = new PersonalityService();
        CsvParticipantRepository csvRepo = new CsvParticipantRepository(validationService);

        // 3) Load participants from CSV (already validated structurally)
        ParticipantLoadResult csvResult = csvRepo.loadAll(filePath);
        List<Participant> validFromCsv = csvResult.getValidParticipants();
        List<InvalidRow> invalidFromCsv = csvResult.getInvalidRows();

        System.out.println("=== CSV LOAD RESULT ===");
        System.out.println("Valid participants (CSV): " + validFromCsv.size());
        System.out.println("Invalid rows (CSV): " + invalidFromCsv.size());
        System.out.println();

        // 4) Run concurrency preprocessing (simulating the real pipeline)
        List<String> rawLines = CsvUtils.readAllLines(filePath);
        ConcurrencyService concurrencyService = new ConcurrencyService(personalityService, validationService);

        ParticipantLoadResult processedResult =
                concurrencyService.processParticipants(validFromCsv, rawLines);

        List<Participant> cleanedParticipants = processedResult.getValidParticipants();
        List<InvalidRow> invalidAfterConcurrency = processedResult.getInvalidRows();

        System.out.println("=== AFTER CONCURRENCY ===");
        System.out.println("Valid participants (processed): " + cleanedParticipants.size());
        System.out.println("Invalid rows (processed): " + invalidAfterConcurrency.size());
        System.out.println();

        // 5) Form balanced teams
        int teamSize = 5; // you can change this to 4, 6, etc.
        TeamFormationService teamService = new TeamFormationService();

        List<Team> teams = teamService.formBalancedTeams(cleanedParticipants, teamSize);

        double globalAvg = teamService.calculateGlobalAverageSkill(cleanedParticipants);

        // 6) Print team summaries
        System.out.println("=== FORMED TEAMS ===");
        System.out.println("Total teams: " + teams.size());
        System.out.println("Global average skill: " + globalAvg);
        System.out.println();

        for (Team team : teams) {
            double teamAvg = team.getAverageSkill();
            double penalty = teamService.computeTeamPenalty(team, globalAvg);

            System.out.println(team.getTeamId()
                    + " | size=" + team.size()
                    + " | avgSkill=" + String.format("%.2f", teamAvg)
                    + " | penalty=" + String.format("%.2f", penalty));

            team.getMembers().forEach(p ->
                    System.out.println("  - " + p.getId()
                            + " | " + p.getName()
                            + " | Role=" + p.getPreferredRole()
                            + " | Game=" + p.getPreferredGame()
                            + " | Skill=" + p.getSkillLevel()
                            + " | Pers=" + p.getPersonalityType())
            );
            System.out.println();
        }

        concurrencyService.shutdown();
    }
}
