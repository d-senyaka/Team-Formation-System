package com.equalize.service;

import com.equalize.exception.ValidationException;
import com.equalize.model.Participant;
import com.equalize.repository.CsvParticipantRepository;
import com.equalize.repository.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantServiceTest {

    @TempDir
    Path tempDir;

    private Path csvPath;
    private ParticipantService participantService;
    private ValidationService validationService;  // Add this line

    @BeforeEach
    void setUp() throws IOException {
        csvPath = tempDir.resolve("participants_test.csv");

        // initial CSV
        String header = "ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType\n";
        String row1 = "P001,Alice,user1@university.edu,FIFA,4,ATTACKER,60,THINKER\n";
        String row2 = "P002,Bob,user2@university.edu,CSGO,6,COORDINATOR,64,THINKER\n";

        Files.writeString(csvPath, header + row1 + row2);

        validationService = new ValidationService();  // Initialize validation service
        ParticipantRepository repo = new CsvParticipantRepository(validationService);  // Pass it to the constructor
        participantService = new ParticipantService(repo, csvPath.toString());
    }

    @Test
    void loadAll_readsValidParticipants() {
        List<Participant> participants = participantService.loadAll();
        assertEquals(2, participants.size());
    }

    @Test
    void findByIdAndEmail_returnsParticipantIfExists() {
        Optional<Participant> p = participantService.findByIdAndEmail("P001", "user1@university.edu");
        assertTrue(p.isPresent());
        assertEquals("P001", p.get().getId());
    }

    @Test
    void createNewParticipant_successWhenUnique() throws IOException {
        Participant p = participantService.createNewParticipant("P010", "user10@university.edu");

        assertEquals("P010", p.getId());
        assertEquals("user10@university.edu", p.getEmail());

        // 1) loadAll returns only *valid* participants (2 in this setup)
        assertEquals(2, participantService.loadAll().size());

        // 2) but the CSV file now has header + 3 rows = 4 lines
        List<String> lines = Files.readAllLines(csvPath);
        assertEquals(4, lines.size());
    }


    @Test
    void createNewParticipant_sameIdAndEmail_throwsValidationException() {
        assertThrows(ValidationException.class, () ->
                participantService.createNewParticipant("P001", "user1@university.edu"));
    }

    @Test
    void createNewParticipant_sameIdDifferentEmail_throwsValidationException() {
        assertThrows(ValidationException.class, () ->
                participantService.createNewParticipant("P001", "other@university.edu"));
    }

    @Test
    void createNewParticipant_differentIdSameEmail_throwsValidationException() {
        assertThrows(ValidationException.class, () ->
                participantService.createNewParticipant("P010", "user1@university.edu"));
    }
}
