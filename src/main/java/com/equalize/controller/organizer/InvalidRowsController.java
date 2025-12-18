package com.equalize.controller.organizer;

import com.equalize.config.AppConfig;
import com.equalize.controller.common.RootLayoutController;
import com.equalize.model.dto.InvalidRow;
import com.equalize.service.OrganizerContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class InvalidRowsController {

    @FXML
    private Label summaryLabel;

    @FXML
    private TableView<InvalidRow> invalidTable;

    @FXML
    private TableColumn<InvalidRow, Integer> rowNumberColumn;

    @FXML
    private TableColumn<InvalidRow, String> rawLineColumn;

    @FXML
    private TableColumn<InvalidRow, String> errorColumn;

    private AppConfig appConfig;
    private RootLayoutController rootLayoutController;
    private OrganizerContext organizerContext;

    public void init(AppConfig appConfig, RootLayoutController rootLayoutController) {
        this.appConfig = appConfig;
        this.rootLayoutController = rootLayoutController;
        this.organizerContext = appConfig.getOrganizerContext();

        setupTable();
        loadData();
    }

    private void setupTable() {
        // PropertyValueFactory uses reflection; property names must match fields/getters
        // in InvalidRow (e.g. getRowNumber(), getRawLine(), getErrorMessage()).
        rowNumberColumn.setCellValueFactory(new PropertyValueFactory<>("rowNumber"));
        rawLineColumn.setCellValueFactory(new PropertyValueFactory<>("rawLine"));
        errorColumn.setCellValueFactory(new PropertyValueFactory<>("errorMessage"));
    }

    private void loadData() {
        if (organizerContext == null) {
            summaryLabel.setText("No data loaded.");
            invalidTable.setItems(FXCollections.observableArrayList());
            return;
        }

        ObservableList<InvalidRow> items =
                FXCollections.observableArrayList(organizerContext.getInvalidRows());

        invalidTable.setItems(items);

        int count = items.size();
        summaryLabel.setText("Total invalid rows: " + count);
    }

    @FXML
    private void handleBackToDashboard() {
        if (rootLayoutController != null) {
            rootLayoutController.showOrganizerDashboard();
        }
    }

    @FXML
    private void handleBackToDataSource() {
        if (rootLayoutController != null) {
            rootLayoutController.showOrganizerDataSource();
        }
    }
}
