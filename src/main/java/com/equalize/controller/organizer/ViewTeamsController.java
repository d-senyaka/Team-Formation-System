package com.equalize.controller.organizer;

import com.equalize.config.AppConfig;
import com.equalize.controller.common.RootLayoutController;
import com.equalize.model.Participant;
import com.equalize.model.Team;
import com.equalize.repository.TeamRepository;
import com.equalize.service.OrganizerContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class ViewTeamsController {

    @FXML
    private Label teamCountLabel;

    @FXML
    private Label participantCountLabel;

    @FXML
    private Label selectedTeamLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private ListView<String> teamListView;

    @FXML
    private TableView<Participant> memberTable;

    @FXML
    private TableColumn<Participant, String> idColumn;

    @FXML
    private TableColumn<Participant, String> nameColumn;

    @FXML
    private TableColumn<Participant, String> gameColumn;

    @FXML
    private TableColumn<Participant, Integer> skillColumn;

    @FXML
    private TableColumn<Participant, String> roleColumn;

    @FXML
    private TableColumn<Participant, String> personalityColumn;

    private AppConfig appConfig;
    private RootLayoutController rootLayoutController;
    private OrganizerContext organizerContext;
    private TeamRepository teamRepository;

    // Keep actual Team objects parallel to the ListView items
    private List<Team> teams = new ArrayList<>();

    public void init(AppConfig appConfig, RootLayoutController rootLayoutController) {
        this.appConfig = appConfig;
        this.rootLayoutController = rootLayoutController;
        this.organizerContext = appConfig.getOrganizerContext();
        this.teamRepository = appConfig.getTeamRepository();

        setupMemberTable();
        loadTeamsFromContext();
        setupTeamSelectionListener();
    }

    private void setupMemberTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        gameColumn.setCellValueFactory(new PropertyValueFactory<>("preferredGame"));
        skillColumn.setCellValueFactory(new PropertyValueFactory<>("skillLevel"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("preferredRole"));
        personalityColumn.setCellValueFactory(new PropertyValueFactory<>("personalityType"));
    }

    private void loadTeamsFromContext() {
        errorLabel.setText("");
        statusLabel.setText("");

        if (organizerContext == null || organizerContext.getFormedTeams() == null) {
            teamCountLabel.setText("0");
            participantCountLabel.setText("0");
            teamListView.setItems(FXCollections.observableArrayList());
            memberTable.setItems(FXCollections.observableArrayList());
            selectedTeamLabel.setText("No team selected");
            statusLabel.setText("No teams formed yet.");
            return;
        }

        teams = organizerContext.getFormedTeams();
        int teamCount = teams.size();
        int participantCount = teams.stream()
                .mapToInt(t -> t.getMembers().size())
                .sum();

        teamCountLabel.setText(String.valueOf(teamCount));
        participantCountLabel.setText(String.valueOf(participantCount));

        List<String> displayStrings = new ArrayList<>();
        for (Team t : teams) {
            int size = t.getMembers().size();
            double avgSkill = t.getMembers().stream()
                    .mapToInt(Participant::getSkillLevel)
                    .average()
                    .orElse(0.0);
            displayStrings.add(t.getTeamId() + " (" + size + " members, avg skill " +
                    String.format("%.2f", avgSkill) + ")");
        }

        teamListView.setItems(FXCollections.observableArrayList(displayStrings));

        if (!teams.isEmpty()) {
            teamListView.getSelectionModel().selectFirst();
            showTeamAtIndex(0);
        } else {
            selectedTeamLabel.setText("No team selected");
            memberTable.setItems(FXCollections.observableArrayList());
        }
    }

    private void setupTeamSelectionListener() {
        teamListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldV, newV) -> {
            int index = newV.intValue();
            if (index >= 0 && index < teams.size()) {
                showTeamAtIndex(index);
            }
        });
    }

    private void showTeamAtIndex(int index) {
        Team team = teams.get(index);
        selectedTeamLabel.setText(team.getTeamId());

        ObservableList<Participant> members =
                FXCollections.observableArrayList(team.getMembers());
        memberTable.setItems(members);
    }

    @FXML
    private void handleExport() {
        errorLabel.setText("");
        statusLabel.setText("");

        if (teams == null || teams.isEmpty()) {
            errorLabel.setText("No teams to export.");
            return;
        }

        try {
            teamRepository.saveAll(teams);
            statusLabel.setText("Teams exported to CSV successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Failed to export teams: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackToDashboard() {
        if (rootLayoutController != null) {
            rootLayoutController.showOrganizerDashboard();
        }
    }
}
