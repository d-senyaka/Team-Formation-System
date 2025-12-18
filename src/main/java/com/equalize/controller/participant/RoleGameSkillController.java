package com.equalize.controller.participant;

import com.equalize.config.AppConfig;
import com.equalize.controller.common.RootLayoutController;
import com.equalize.model.Participant;
import com.equalize.model.enums.GameType;
import com.equalize.model.enums.RoleType;
import com.equalize.service.ParticipantService;
import com.equalize.service.ParticipantSession;
import com.equalize.service.ValidationService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for participant_role_game_skill.fxml.
 * Lets the participant choose preferred role, game and skill,
 * validates, and persists the participant record.
 */
public class RoleGameSkillController {

    @FXML
    private ComboBox<RoleType> roleCombo;

    @FXML
    private ComboBox<GameType> gameCombo;

    @FXML
    private Slider skillSlider;

    @FXML
    private Label errorLabel;

    @FXML
    private Label infoLabel;

    private ParticipantSession participantSession;
    private ParticipantService participantService;
    private ValidationService validationService;
    private RootLayoutController rootLayoutController;

    public void init(AppConfig appConfig, RootLayoutController rootLayoutController) {
        this.participantSession = appConfig.getParticipantSession();
        this.participantService = appConfig.getParticipantService();
        this.validationService = appConfig.getValidationService();
        this.rootLayoutController = rootLayoutController;

        // Populate combo boxes with enum values
        roleCombo.setItems(FXCollections.observableArrayList(RoleType.values()));
        gameCombo.setItems(FXCollections.observableArrayList(GameType.values()));
    }

    @FXML
    private void handleBackToSurvey(ActionEvent event) {
        if (rootLayoutController != null) {
            rootLayoutController.showParticipantSurvey();
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        errorLabel.setText("");
        infoLabel.setText("");

        if (participantSession == null || !participantSession.isLoggedIn()) {
            errorLabel.setText("No participant in session. Please login again.");
            return;
        }

        Participant participant = participantSession.getCurrentParticipant();

        RoleType selectedRole = roleCombo.getValue();
        GameType selectedGame = gameCombo.getValue();
        int skill = (int) skillSlider.getValue();

        if (selectedRole == null || selectedGame == null) {
            errorLabel.setText("Please select a role and a game.");
            return;
        }

        // Update participant object
        participant.setPreferredRole(selectedRole);
        participant.setPreferredGame(selectedGame);
        participant.setSkillLevel(skill);

        // Validate using ValidationService
        List<String> errors = validationService.validateParticipant(participant);
        if (!errors.isEmpty()) {
            String combined = errors.stream().collect(Collectors.joining("\n"));
            errorLabel.setText(combined);
            return;
        }

        // Save or update in repository
        List<Participant> all = participantService.loadAll();
        Optional<Participant> existing = all.stream()
                .filter(p -> participant.getId().equalsIgnoreCase(p.getId())
                        && participant.getEmail().equalsIgnoreCase(p.getEmail()))
                .findFirst();

        if (existing.isPresent()) {
            int index = all.indexOf(existing.get());
            all.set(index, participant);
        } else {
            all.add(participant);
        }

        participantService.saveAll(all);

        infoLabel.setText("Preferences saved successfully.");

        if (rootLayoutController != null) {
            rootLayoutController.showParticipantComplete();
        }
    }
}
