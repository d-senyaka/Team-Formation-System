package com.teame;

import com.teame.config.AppConfig;
import com.teame.controller.common.RootLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1. Load root layout
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/teame/fxml/root_layout.fxml")
        );
        BorderPane rootLayout = loader.load();

        // 2. Get controller & inject AppConfig
        RootLayoutController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        AppConfig appConfig = new AppConfig();
        controller.init(appConfig);

        // 3. Show root layout
        Scene scene = new Scene(rootLayout, 900, 600);
        primaryStage.setTitle("Intelligent Team Formation System");
        primaryStage.setScene(scene);
        primaryStage.show();

        // 4. Show first screen (participant login OR organizer dashboard)
        // For testing: switch between these
        controller.showParticipantLogin();
        // controller.showOrganizerDashboard(); // uncomment to test organizer
    }

    public static void main(String[] args) {
        launch(args);
    }
}
