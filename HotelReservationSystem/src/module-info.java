module HotelReservationSystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.prefs;
    requires java.sql;
    // requires jakarta.xml.bind;

    opens application to javafx.graphics, javafx.fxml;
    opens controllers to javafx.fxml;

    exports controllers to javafx.fxml, javafx.graphics;
    exports models to javafx.fxml, javafx.graphics;
}