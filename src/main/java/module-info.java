module org.oop.teameintelligentteamformation {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    // FXML controllers
    opens com.equalize to javafx.fxml;
    opens com.equalize.controller.common to javafx.fxml;
    opens com.equalize.controller.participant to javafx.fxml;
    opens com.equalize.controller.organizer to javafx.fxml;

    // Model classes used by TableView / PropertyValueFactory (Participant, User, etc.)
    opens com.equalize.model to javafx.base, javafx.fxml;

    // (optional) exports – only if you need them outside the module
    exports com.equalize;
    exports com.equalize.controller.common;
    exports com.equalize.controller.participant;
    exports com.equalize.controller.organizer;
    exports com.equalize.model;
}
