package com.equalize;

import com.equalize.config.AppConfig;
import com.equalize.controller.common.RootLayoutController;
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
                getClass().getResource("/com/equalize/fxml/root_layout.fxml")
        );
        BorderPane rootLayout = loader.load();

        // 2. Get controller & inject AppConfig
        RootLayoutController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        AppConfig appConfig = new AppConfig();
        controller.init(appConfig);

        // 3. Create scene using the pref size from root_layout.fxml
        Scene scene = new Scene(rootLayout);
        primaryStage.setTitle("Intelligent Team Formation System");
        primaryStage.setScene(scene);

        // Let the scene (and BorderPane prefWidth/prefHeight) decide size
        primaryStage.sizeToScene();

        // Optional but recommended: minimum size so user can't shrink too much
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);

        primaryStage.show();

        // 4. Show first screen (participant login OR organizer dashboard)
        controller.showParticipantLogin();
        // controller.showOrganizerDashboard(); // uncomment to test organizer
    }

    public static void main(String[] args) {
        launch(args);
    }
}
