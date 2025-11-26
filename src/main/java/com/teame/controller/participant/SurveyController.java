package com.teame.controller.participant;

import com.teame.config.AppConfig;
import com.teame.controller.common.RootLayoutController;
import com.teame.model.Participant;
import com.teame.model.enums.PersonalityType;
import com.teame.service.ParticipantSession;
import com.teame.service.PersonalityService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

/**
 * Controller for participant_survey.fxml.
 * Collects 5 answers, computes personality score and type,
 * stores them in the current Participant, then moves to the role/game/skill screen.
 */
public class SurveyController {

    @FXML
    private Slider q1Slider;

    @FXML
    private Slider q2Slider;

    @FXML
    private Slider q3Slider;

    @FXML
    private Slider q4Slider;

    @FXML
    private Slider q5Slider;

    @FXML
    private Label infoLabel;

    private PersonalityService personalityService;
    private ParticipantSession participantSession;
    private RootLayoutController rootLayoutController;

    public void init(AppConfig appConfig, RootLayoutController rootLayoutController) {
        this.personalityService = appConfig.getPersonalityService();
        this.participantSession = appConfig.getParticipantSession();
        this.rootLayoutController = rootLayoutController;
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        if (rootLayoutController != null) {
            rootLayoutController.showParticipantLogin();
        }
    }

    @FXML
    private void handleNext(ActionEvent event) {
        if (participantSession == null || !participantSession.isLoggedIn()) {
            infoLabel.setText("No participant in session. Please login again.");
            return;
        }

        int q1 = (int) q1Slider.getValue();
        int q2 = (int) q2Slider.getValue();
        int q3 = (int) q3Slider.getValue();
        int q4 = (int) q4Slider.getValue();
        int q5 = (int) q5Slider.getValue();

        // 1) raw score 5–25
        int rawScore = personalityService.calculateScore(q1, q2, q3, q4, q5);

        // 2) scale to 0–100 as per brief (raw * 4)
        int scaledScore = personalityService.scaleToHundred(rawScore);

        // 3) classify using the 90/70/50 ranges
        PersonalityType type = personalityService.classifyScore(scaledScore);

        // 4) store SCALED score + type in the participant
        Participant participant = participantSession.getCurrentParticipant();
        participant.setPersonalityScore(scaledScore);
        participant.setPersonalityType(type);

        infoLabel.setText(
                "Personality: " + type +
                        " (raw " + rawScore + ", scaled " + scaledScore + ")"
        );

        // Navigate to next screen: role/game/skill
        if (rootLayoutController != null) {
            rootLayoutController.showParticipantRoleGameSkill();
        }
    }

}
