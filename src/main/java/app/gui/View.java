package app.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The entry point for the JavaFX GUI application.
 * <p>
 * Initializes and displays the main application window using an FXML layout.
 * It loads the GUI from {@code main-view.fxml} and sets the stage dimensions and title.
 */
public class View extends Application {

    /**
     * Starts the JavaFX application by loading the main view from FXML and displaying the primary stage.
     *
     * @param stage the primary stage provided by the JavaFX runtime
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(View.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 820, 620);
        stage.setTitle("Orders Management");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args the command-line arguments, which are not used
     */
    public static void main(String[] args) {
        launch();
    }
}