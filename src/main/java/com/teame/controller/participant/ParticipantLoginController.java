package com.teame.controller.participant;

import com.teame.config.AppConfig;
import com.teame.controller.common.RootLayoutController;
import com.teame.exception.ValidationException;
import com.teame.model.Participant;
import com.teame.service.ParticipantService;
import com.teame.service.ParticipantSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import java.util.Optional;

import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

import java.util.regex.Pattern;



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

    // ID: letters and digits only (no emoji, spaces, or symbols)
    private static final Pattern ID_PATTERN =
            Pattern.compile("^[A-Za-z0-9]+$");

    // University email, e.g. user@university.edu
    private static final Pattern UNIVERSITY_EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@university\\.edu$");


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
        // Step 1: Confirm they are an organizer
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Organizer Mode");
        confirm.setHeaderText("Are you an organizer?");
        confirm.setContentText(
                "Click Yes if you are an organizer.\n" +
                        "Click No to stay on the participant login screen."
        );

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirm.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> confirmResult = confirm.showAndWait();

        if (!confirmResult.isPresent() || confirmResult.get() != yesButton) {
            // User said No or closed the dialog → stay on login screen
            errorLabel.setText("Organizer mode cancelled.");
            return;
        }

        // Step 2: Show organizer credentials dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Organizer Login");
        dialog.setHeaderText("Enter organizer credentials");

        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().setAll(loginButtonType, ButtonType.CANCEL);

        // Create the form (name + password)
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField organizerNameField = new TextField();
        organizerNameField.setPromptText("Organizer name");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        grid.add(new Label("Organizer name:"), 0, 0);
        grid.add(organizerNameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> loginResult = dialog.showAndWait();

        if (!loginResult.isPresent() || loginResult.get() != loginButtonType) {
            // User cancelled organizer login → stay on login menu
            errorLabel.setText("Organizer login cancelled.");
            return;
        }

        String organizerName = organizerNameField.getText() != null
                ? organizerNameField.getText().trim()
                : "";
        String password = passwordField.getText() != null
                ? passwordField.getText().trim()
                : "";

        // Step 3: Hard-coded credentials check
        if ("admin".equals(organizerName) && "admin".equals(password)) {
            // Correct organizer credentials → go to organizer dashboard
            errorLabel.setText(""); // clear any old error
            if (rootLayoutController != null) {
                rootLayoutController.showOrganizerDashboard();
            }
        } else {
            // Wrong credentials → show error and stay on login
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Organizer Login Failed");
            errorAlert.setHeaderText("Invalid organizer credentials");
            errorAlert.setContentText("Organizer name or password is incorrect.");
            errorAlert.showAndWait();

            errorLabel.setText("Organizer login failed. Please try again.");
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





    private void handleLoginOrCreate(boolean createIfMissing) {
        String id = idField.getText() != null ? idField.getText().trim() : "";
        String email = emailField.getText() != null ? emailField.getText().trim() : "";

        // Basic validation
        if (id.isEmpty() || email.isEmpty()) {
            errorLabel.setText("ID and Email are required.");
            return;
        }

        // ID must be letters + numbers only
        if (!ID_PATTERN.matcher(id).matches()) {
            errorLabel.setText("ID can only contain letters and numbers (no spaces or symbols).");
            return;
        }

        // Email must be a valid university email
        if (!UNIVERSITY_EMAIL_PATTERN.matcher(email).matches()) {
            errorLabel.setText("Please enter a valid university email (e.g. user@university.edu).");
            return;
        }


        Participant participant;

        if (createIfMissing) {
            // --- CREATE ACCOUNT PATH ---
            try {
                participant = participantService.createNewParticipant(id, email);
            } catch (ValidationException ex) {
                // Show a friendly message from the service
                errorLabel.setText(ex.getMessage());
                return;
            }

        } else {
            // --- LOGIN PATH ---
            participant = participantService.findByIdAndEmail(id, email)
                    .orElse(null);

            if (participant == null) {
                errorLabel.setText("You haven't created an account yet. Please click 'Create Account' and proceed.");
                return;
            }

            // Existing account found → ask if they want to update details
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Existing Account");
            alert.setHeaderText("You already have an account.");
            alert.setContentText("Do you want to update the associated details?\n\n"
                    + "Click Yes if you want to update them.\n"
                    + "Click No to cancel login and keep your existing details.");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(yesButton, noButton);

            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() == noButton) {
                // User cancelled login → stay on login screen
                errorLabel.setText("Login cancelled. Your existing details were not changed.");
                return;
            }

            // If they clicked Yes, we just continue normally:
            // participantSession will be set and we’ll go to the survey.
        }


        // Store in session
        participantSession.setCurrentParticipant(participant);

        // Navigate to survey screen
        if (rootLayoutController != null) {
            rootLayoutController.showParticipantSurvey();
        }
    }

}
