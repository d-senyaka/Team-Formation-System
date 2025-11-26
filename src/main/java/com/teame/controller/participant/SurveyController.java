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

        int score = personalityService.calculateScore(q1, q2, q3, q4, q5);
        PersonalityType type = personalityService.classify(score);

        Participant participant = participantSession.getCurrentParticipant();
        participant.setPersonalityScore(score);
        participant.setPersonalityType(type);

        infoLabel.setText("Personality: " + type + " (score " + score + ")");

        // Navigate to next screen: role/game/skill
        if (rootLayoutController != null) {
            rootLayoutController.showParticipantRoleGameSkill();
        }
    }
}
