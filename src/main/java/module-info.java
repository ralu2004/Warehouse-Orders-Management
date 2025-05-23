module app {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;

    opens app.gui to javafx.fxml;

    exports app.gui;
    exports app.gui.controllers;
    opens app.gui.controllers to javafx.fxml;
}