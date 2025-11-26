package com.teame.controller.organizer;

import com.teame.config.AppConfig;
import com.teame.concurrency.ConcurrencyService;
import com.teame.controller.common.RootLayoutController;
import com.teame.model.Participant;
import com.teame.model.Team;
import com.teame.model.dto.InvalidRow;
import com.teame.repository.ParticipantLoadResult;
import com.teame.repository.ParticipantRepository;
import com.teame.service.OrganizerContext;
import com.teame.util.CsvUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataSourceController {

    @FXML
    private Label sourceFileLabel;

    @FXML
    private Label validCountLabel;

    @FXML
    private Label invalidCountLabel;

    @FXML
    private Label errorLabel;

    private AppConfig appConfig;
    private RootLayoutController rootLayoutController;

    private OrganizerContext organizerContext;
    private ParticipantRepository participantRepository;
    private ConcurrencyService concurrencyService;

    public void init(AppConfig appConfig, RootLayoutController rootLayoutController) {
        this.appConfig = appConfig;
        this.rootLayoutController = rootLayoutController;

        this.organizerContext = appConfig.getOrganizerContext();
        this.participantRepository = appConfig.getParticipantRepository();
        this.concurrencyService = appConfig.getConcurrencyService();

        refreshFromContext();
    }

    @FXML
    private void handleBackToDashboard() {
        if (rootLayoutController != null) {
            rootLayoutController.showOrganizerDashboard();
        }
    }

    @FXML
    private void handleLoadDefault() {
        String path = appConfig.getParticipantsFilePath();
        loadAndProcess(path);
    }

    @FXML
    private void handleLoadExternal() {
        try {
            Stage stage = (Stage) rootLayoutController.getRootLayout().getScene().getWindow();
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select participant CSV");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("CSV Files", "*.csv")
            );
            File file = chooser.showOpenDialog(stage);
            if (file != null) {
                loadAndProcess(file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error opening file chooser: " + e.getMessage());
        }
    }

    // === Core loading + concurrency pipeline ===

    private void loadAndProcess(String filePath) {
        errorLabel.setText("");
        try {
            // 1) Initial load from CSV using ParticipantRepository
            ParticipantLoadResult loadResult = participantRepository.loadAll(filePath);

            List<Participant> initiallyValid = loadResult.getValidParticipants();
            List<InvalidRow> combinedInvalidRows =
                    new ArrayList<>(loadResult.getInvalidRows());

            // 2) Read raw CSV lines for concurrency service
            List<String> allLines = CsvUtils.readAllLines(filePath);
            List<String> rawLines;
            if (allLines.size() > 1) {
                // skip header line (index 0) – ConcurrencyService expects data-only
                rawLines = allLines.subList(1, allLines.size());
            } else {
                rawLines = Collections.emptyList();
            }

            // 3) Run concurrency pipeline on the valid participants
            ParticipantLoadResult processedResult =
                    concurrencyService.processParticipants(initiallyValid, rawLines);

            List<Participant> cleaned = processedResult.getValidParticipants();
            List<InvalidRow> invalidFromProcessing = processedResult.getInvalidRows();
            combinedInvalidRows.addAll(invalidFromProcessing);

            // 4) Store in OrganizerContext
            organizerContext.setSourceFilePath(filePath);
            organizerContext.setCleanedParticipants(cleaned);
            organizerContext.setInvalidRows(combinedInvalidRows);
            organizerContext.setFormedTeams(new ArrayList<Team>());

            // 5) Update labels
            sourceFileLabel.setText(filePath);
            validCountLabel.setText(String.valueOf(cleaned.size()));
            invalidCountLabel.setText(String.valueOf(combinedInvalidRows.size()));

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load/process CSV: " + e.getMessage());
        }
    }

    private void refreshFromContext() {
        if (organizerContext == null || organizerContext.getSourceFilePath() == null) {
            sourceFileLabel.setText("(no file loaded)");
            validCountLabel.setText("0");
            invalidCountLabel.setText("0");
        } else {
            sourceFileLabel.setText(organizerContext.getSourceFilePath());
            validCountLabel.setText(
                    String.valueOf(organizerContext.getCleanedParticipants().size())
            );
            invalidCountLabel.setText(
                    String.valueOf(organizerContext.getInvalidRows().size())
            );
        }
        errorLabel.setText("");
    }
}
