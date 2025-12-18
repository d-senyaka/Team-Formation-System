package com.equalize.controller.organizer;

import com.equalize.config.AppConfig;
import com.equalize.controller.common.RootLayoutController;
import javafx.fxml.FXML;

/**
 * Simple menu hub for organizer actions.
 */
public class OrganizerDashboardController {

    private AppConfig appConfig;
    private RootLayoutController rootLayoutController;

    public void init(AppConfig appConfig, RootLayoutController rootLayoutController) {
        this.appConfig = appConfig;
        this.rootLayoutController = rootLayoutController;
    }

    @FXML
    private void handleOpenDataSource() {
        if (rootLayoutController != null) {
            rootLayoutController.showOrganizerDataSource();
        }
    }

    @FXML
    private void handleOpenInvalidRows() {
        if (rootLayoutController != null) {
            rootLayoutController.showOrganizerInvalidRows();
        }
    }

    @FXML
    private void handleOpenTeamFormation() {
        if (rootLayoutController != null) {
            rootLayoutController.showOrganizerTeamFormation();
        }
    }

    @FXML
    private void handleOpenViewTeams() {
        if (rootLayoutController != null) {
            rootLayoutController.showOrganizerViewTeams();
        }
    }

    @FXML
    private void handleBackToParticipant() {
        if (rootLayoutController != null) {
            rootLayoutController.showParticipantLogin();
        }
    }
}
