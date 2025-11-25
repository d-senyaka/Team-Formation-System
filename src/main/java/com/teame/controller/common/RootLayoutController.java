package com.teame.controller.common;

import com.teame.config.AppConfig;
import com.teame.controller.participant.ParticipantLoginController;
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
        // We will implement this in Phase 6.3
    }
}
