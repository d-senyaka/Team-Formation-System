package com.teame.controller.common;

import com.teame.config.AppConfig;
import com.teame.controller.participant.ParticipantLoginController;
import com.teame.controller.participant.SurveyController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.Node;

import java.io.IOException;

public class RootLayoutController {

    @FXML
    private BorderPane rootLayout; // make sure your root_layout.fxml has fx:id="rootLayout"

    private final AppConfig appConfig = new AppConfig();

    public void initialize() {
        // Optionally show login on startup
        showParticipantLogin();
    }

    public void showParticipantLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/teame/fxml/participant/participant_login.fxml"));
            Node view = loader.load();

            ParticipantLoginController controller = loader.getController();
            controller.init(appConfig, this);

            rootLayout.setCenter(view);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This will be used by the login controller
    public void showParticipantSurvey() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/teame/fxml/participant/participant_survey.fxml"));
            Node view = loader.load();

            SurveyController controller = loader.getController();
            controller.init(appConfig, this);

            rootLayout.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Will be implemented properly in 6.4
    public void showParticipantRoleGameSkill() {
        // TODO: load participant_role_game_skill.fxml in Phase 6.4
    }
}
