package com.equalize.controller.organizer;

import com.equalize.config.AppConfig;
import com.equalize.controller.common.RootLayoutController;
import com.equalize.model.Participant;
import com.equalize.model.Team;
import com.equalize.service.OrganizerContext;
import com.equalize.service.TeamFormationService;
import com.equalize.repository.TeamRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;

public class TeamFormationController {

    @FXML
    private Label validCountLabel;

    @FXML
    private Label estimatedTeamsLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField teamSizeField;

    private AppConfig appConfig;
    private RootLayoutController rootLayoutController;

    private OrganizerContext organizerContext;
    private TeamFormationService teamFormationService;
    private TeamRepository teamRepository;

    public void init(AppConfig appConfig, RootLayoutController rootLayoutController) {
        this.appConfig = appConfig;
        this.rootLayoutController = rootLayoutController;

        this.organizerContext = appConfig.getOrganizerContext();
        this.teamFormationService = appConfig.getTeamFormationService();
        this.teamRepository = appConfig.getTeamRepository();

        refreshSummary();
    }

    private void refreshSummary() {
        if (organizerContext == null) {
            validCountLabel.setText("0");
            estimatedTeamsLabel.setText("-");
            statusLabel.setText("No data loaded.");
            return;
        }

        int validCount = organizerContext.getCleanedParticipants().size();
        validCountLabel.setText(String.valueOf(validCount));

        String teamSizeText = teamSizeField.getText();
        int teamSize = parseTeamSize(teamSizeText);
        if (teamSize > 0 && validCount > 0) {
            int estimated = (int) Math.ceil((double) validCount / teamSize);
            estimatedTeamsLabel.setText(String.valueOf(estimated));
        } else {
            estimatedTeamsLabel.setText("-");
        }

        int existingTeams = organizerContext.getFormedTeams().size();
        if (existingTeams > 0) {
            statusLabel.setText("Already formed " + existingTeams + " teams.");
        } else {
            statusLabel.setText("No teams formed yet.");
        }

        errorLabel.setText("");
    }

    private int parseTeamSize(String text) {
        try {
            int value = Integer.parseInt(text.trim());
            return value > 0 ? value : -1;
        } catch (Exception e) {
            return -1;
        }
    }

    @FXML
    private void handleFormTeams() {
        errorLabel.setText("");

        if (organizerContext == null) {
            errorLabel.setText("No participant data loaded.");
            return;
        }

        List<Participant> participants = organizerContext.getCleanedParticipants();
        if (participants == null || participants.isEmpty()) {
            errorLabel.setText("No valid participants available.");
            return;
        }

        int teamSize = parseTeamSize(teamSizeField.getText());
        if (teamSize <= 0) {
            errorLabel.setText("Please enter a valid positive team size (e.g. 4 or 5).");
            return;
        }

        // Run team formation
        List<Team> teams = teamFormationService.formBalancedTeams(participants, teamSize);

        // Save to context
        organizerContext.setFormedTeams(teams);

        // Persist to CSV
        try {
            teamRepository.saveAll(teams);
            statusLabel.setText("Formed " + teams.size() + " teams and saved to CSV.");
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Teams formed, but failed to save CSV: " + e.getMessage());
        }

        refreshSummary();
    }

    @FXML
    private void handleBackToDashboard() {
        if (rootLayoutController != null) {
            rootLayoutController.showOrganizerDashboard();
        }
    }

    @FXML
    private void initialize() {
        // When the field changes, update estimated teams
        if (teamSizeField != null) {
            teamSizeField.textProperty().addListener((obs, oldV, newV) -> refreshSummary());
        }
    }
}
