package edu.westga.studyhub.view.codebehind;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

import edu.westga.studyhub.model.StudyGroup;
import edu.westga.studyhub.Main;

/**
 * CodeBehind to handle processing for the MainWindow.
 *
 * @author Moria El Akaya
 * @version Fall 2026
 */
public class MainWindow {
    @FXML private ListView<StudyGroup> studyGroups;
    @FXML private AnchorPane guiPane;

    @FXML
    void addGroup(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(Main.ADD_GROUP_WINDOW_RESOURCE));
            loader.load();
            Parent parent = loader.getRoot();
            Scene scene = new Scene(parent);
            Stage addGroupStage = new Stage();
            addGroupStage.setTitle(Main.WINDOW_TITLE);
            addGroupStage.setScene(scene);
            addGroupStage.initModality(Modality.APPLICATION_MODAL);

            AddGroupWindow controller = (AddGroupWindow) loader.getController();
            controller.setGroupList(this.studyGroups.getItems());

            addGroupStage.showAndWait();
        } catch (IOException error) {
            Alert errorBox = new Alert(AlertType.ERROR);
            errorBox.setContentText("Unable to open the add group window.");
            errorBox.showAndWait();
        }
    }

    @FXML
    void removeGroup(ActionEvent event) {
        StudyGroup selectedGroup = this.studyGroups.getSelectionModel().getSelectedItem();
        if (selectedGroup != null) {
            this.studyGroups.getItems().remove(selectedGroup);
        }
    }

    @FXML
    void initialize() {
        assert this.studyGroups != null : "fx:id=\"studyGroups\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert this.guiPane != null : "fx:id=\"guiPane\" was not injected: check your FXML file 'MainWindow.fxml'.";
    }
}
