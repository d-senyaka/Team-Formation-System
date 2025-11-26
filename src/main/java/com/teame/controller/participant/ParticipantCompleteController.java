package com.teame.controller.participant;

import com.teame.config.AppConfig;
import com.teame.controller.common.RootLayoutController;
import com.teame.model.Participant;
import com.teame.service.ParticipantSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Final summary / thank-you screen for participants.
 */
public class ParticipantCompleteController {

    @FXML
    private Label idLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label personalityLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private Label gameLabel;

    @FXML
    private Label skillLabel;

    private ParticipantSession participantSession;
    private RootLayoutController rootLayoutController;

    public void init(AppConfig appConfig, RootLayoutController rootLayoutController) {
        this.participantSession = appConfig.getParticipantSession();
        this.rootLayoutController = rootLayoutController;

        populateSummary();
    }

    private void populateSummary() {
        if (participantSession == null || !participantSession.isLoggedIn()) {
            idLabel.setText("-");
            nameLabel.setText("-");
            emailLabel.setText("-");
            personalityLabel.setText("-");
            roleLabel.setText("-");
            gameLabel.setText("-");
            skillLabel.setText("-");
            return;
        }

        Participant p = participantSession.getCurrentParticipant();

        idLabel.setText(nullSafe(p.getId()));
        nameLabel.setText(nullSafe(p.getName()));
        emailLabel.setText(nullSafe(p.getEmail()));

        String personalityText = (p.getPersonalityType() != null)
                ? p.getPersonalityType() + " (score " + p.getPersonalityScore() + ")"
                : "-";
        personalityLabel.setText(personalityText);

        roleLabel.setText(p.getPreferredRole() != null ? p.getPreferredRole().name() : "-");
        gameLabel.setText(p.getPreferredGame() != null ? p.getPreferredGame().name() : "-");
        skillLabel.setText(String.valueOf(p.getSkillLevel()));
    }

    private String nullSafe(String value) {
        return value != null ? value : "-";
    }

    @FXML
    private void handleBackToLogin() {
        if (participantSession != null) {
            participantSession.clear();
        }
        if (rootLayoutController != null) {
            rootLayoutController.showParticipantLogin();
        }
    }

    @FXML
    private void handleExit() {
        // Close the app window
        if (rootLayoutController != null && rootLayoutController.getRootLayout() != null) {
            Stage stage = (Stage) rootLayoutController.getRootLayout().getScene().getWindow();
            stage.close();
        }
    }
}
