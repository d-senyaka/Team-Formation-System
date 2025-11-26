package com.teame.controller.participant;

import com.teame.config.AppConfig;
import com.teame.controller.common.RootLayoutController;
import com.teame.model.Participant;
import com.teame.service.ParticipantService;
import com.teame.service.ParticipantSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller for the participant_login.fxml screen.
 * Handles basic login / create account and moves to the survey screen.
 */
public class ParticipantLoginController {

    @FXML
    private TextField idField;

    @FXML
    private TextField emailField;

    @FXML
    private Label errorLabel;

    // References injected from RootLayout/MainApp
    private ParticipantService participantService;
    private ParticipantSession participantSession;
    private RootLayoutController rootLayoutController;

    /**
     * This will be called by RootLayoutController after loading the FXML.
     */
    public void init(AppConfig appConfig, RootLayoutController rootLayoutController) {
        this.participantService = appConfig.getParticipantService();
        this.participantSession = appConfig.getParticipantSession();
        this.rootLayoutController = rootLayoutController;
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        handleLoginOrCreate(false);
    }

    @FXML
    private void handleCreateAccount(ActionEvent event) {
        handleLoginOrCreate(true);
    }

    @FXML
    private void handleOrganizerMode() {
        if (rootLayoutController != null) {
            rootLayoutController.showOrganizerDashboard();
        }
    }


    private void handleLoginOrCreate(boolean createIfMissing) {
        String id = idField.getText() != null ? idField.getText().trim() : "";
        String email = emailField.getText() != null ? emailField.getText().trim() : "";

        // Basic validation
        if (id.isEmpty() || email.isEmpty()) {
            errorLabel.setText("ID and Email are required.");
            return;
        }
        if (!email.contains("@")) {
            errorLabel.setText("Please enter a valid email address.");
            return;
        }

        Participant participant;
        if (createIfMissing) {
            participant = participantService.findOrCreateParticipant(id, email);
        } else {
            participant = participantService.findByIdAndEmail(id, email)
                    .orElse(null);

            if (participant == null) {
                errorLabel.setText("Participant not found. Try 'Create Account'.");
                return;
            }
        }

        // Store in session
        participantSession.setCurrentParticipant(participant);

        // Navigate to survey screen
        if (rootLayoutController != null) {
            rootLayoutController.showParticipantSurvey();
        }
    }
}
