module org.oop.teameintelligentteamformation {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    // FXML controllers
    opens com.teame to javafx.fxml;
    opens com.teame.controller.common to javafx.fxml;
    opens com.teame.controller.participant to javafx.fxml;
    opens com.teame.controller.organizer to javafx.fxml;

    // Model classes used by TableView / PropertyValueFactory (Participant, User, etc.)
    opens com.teame.model to javafx.base, javafx.fxml;

    // (optional) exports – only if you need them outside the module
    exports com.teame;
    exports com.teame.controller.common;
    exports com.teame.controller.participant;
    exports com.teame.controller.organizer;
    exports com.teame.model;
}
