module org.oop.teameintelligentteamformation {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires eu.hansolo.tilesfx;

    opens com.teame.controller.common to javafx.fxml;
    opens com.teame.controller.organizer to javafx.fxml;
    opens com.teame.controller.participant to javafx.fxml;

    exports com.teame;
}