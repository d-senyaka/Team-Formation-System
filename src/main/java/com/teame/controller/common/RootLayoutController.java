package com.teame.controller.common;

import com.teame.controller.participant.RoleGameSkillController;

import com.teame.controller.participant.ParticipantCompleteController;

import com.teame.controller.organizer.OrganizerDashboardController;


import com.teame.config.AppConfig;
import com.teame.controller.participant.ParticipantLoginController;
import com.teame.controller.participant.SurveyController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class RootLayoutController {



    @FXML
    private BorderPane rootLayout; // make sure your root_layout.fxml has fx:id="rootLayout"

    private AppConfig appConfig;

    public void init(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public void initialize() {
        // Do not show any screen here.
        // Main will decide what the first screen is after injecting AppConfig.
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

    public void showParticipantRoleGameSkill() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/teame/fxml/participant/participant_role_game_skill.fxml"));
            Node view = loader.load();

            RoleGameSkillController controller = loader.getController();
            controller.init(appConfig, this);

            rootLayout.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BorderPane getRootLayout() {
        return rootLayout;
    }


    public void showParticipantComplete() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/teame/fxml/participant/participant_complete.fxml"));
            Node view = loader.load();

            ParticipantCompleteController controller = loader.getController();
            controller.init(appConfig, this);

            rootLayout.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showOrganizerDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/teame/fxml/organizer/organizer_dashboard.fxml"));
            Node view = loader.load();

            OrganizerDashboardController controller = loader.getController();
            controller.init(appConfig, this);

            rootLayout.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showOrganizerDataSource() {
        // TODO in 7.3
    }

    public void showOrganizerInvalidRows() {
        // TODO in 7.4
    }

    public void showOrganizerTeamFormation() {
        // TODO in 7.5
    }

    public void showOrganizerViewTeams() {
        // TODO in 7.6
    }

    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }





}
