package edu.westga.studyhub;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Entry point for the StudyHub application.
 *
 * @author Moria El Akaya
 * @version Fall 2026
 */
public class Main extends Application {
    public static final String WINDOW_TITLE = "StudyHub";
    public static final String MAIN_WINDOW_RESOURCE = "view/codebehind/MainWindow.fxml";
    public static final String ADD_GROUP_WINDOW_RESOURCE = "view/codebehind/AddGroupWindow.fxml";

    /**
     * JavaFX entry point.
     *
     * @precondition none
     * @postcondition none
     *
     * @throws IOException if the main window layout cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource(Main.MAIN_WINDOW_RESOURCE));
        Scene scene = new Scene(parent);
        primaryStage.setTitle(Main.WINDOW_TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Primary Java entry point.
     *
     * @precondition none
     * @postcondition none
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Main.launch(args);
    }
}
