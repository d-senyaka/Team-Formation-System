package com.teame.controller.participant;

import com.teame.config.AppConfig;
import com.teame.controller.common.RootLayoutController;
import com.teame.model.Participant;
import com.teame.service.ParticipantSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Optional;

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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Application");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Click Yes to close the application.");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yesButton) {
            System.exit(0);
        }
    }
}
