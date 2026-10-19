package edu.westga.studyhub.view.codebehind;

import java.io.IOException;

import edu.westga.studyhub.Main;
import edu.westga.studyhub.model.Course;
import edu.westga.studyhub.model.GroupManager;
import edu.westga.studyhub.model.StudyGroup;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Displays courses and groups and delegates collection operations to GroupManager.
 *
 * @author Moria El Akaya
 * @version Fall 2026
 */
public class MainWindow {

    @FXML private ComboBox<Course> courseSelector;
    @FXML private TextField courseSearch;
    @FXML private ListView<StudyGroup> studyGroups;
    @FXML private Button detailsButton;
    @FXML private Button deleteButton;
    @FXML private Label resultCount;

    private final GroupManager manager = new GroupManager();

    @FXML
    void initialize() {
        this.courseSelector.getItems().setAll(this.manager.getCourses());
        this.studyGroups.setPlaceholder(new Label("No groups match. Create a group or change your filters."));
        this.detailsButton.disableProperty().bind(this.studyGroups.getSelectionModel().selectedItemProperty().isNull());
        this.deleteButton.disableProperty().bind(this.studyGroups.getSelectionModel().selectedItemProperty().isNull());
        this.courseSelector.valueProperty().addListener((observable, previous, selected) -> this.refreshGroups());
        this.courseSearch.textProperty().addListener((observable, previous, entered) -> this.refreshGroups());
        this.refreshGroups();
    }

    private void refreshGroups() {
        Course selectedCourse = this.courseSelector.getValue();
        String code = selectedCourse == null ? null : selectedCourse.getCode();
        this.studyGroups.getItems().setAll(this.manager.findGroups(code, this.courseSearch.getText()));
        this.resultCount.setText(this.studyGroups.getItems().size() + " groups shown");
    }

    @FXML
    void showAll() {
        this.courseSelector.getSelectionModel().clearSelection();
        this.courseSearch.clear();
        this.refreshGroups();
    }

    @FXML
    void addGroup() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(Main.ADD_GROUP_WINDOW_RESOURCE));
            Parent parent = loader.load();
            AddGroupWindow controller = loader.getController();
            controller.configure(this.manager, this.courseSelector.getValue());
            this.showWindow(parent, "Create study group");
            this.refreshGroups();
        } catch (IOException exception) {
            this.showError("Unable to open the create-group window.");
        }
    }

    @FXML
    void viewDetails() {
        StudyGroup selected = this.studyGroups.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(Main.GROUP_DETAILS_WINDOW_RESOURCE));
            Parent parent = loader.load();
            GroupDetailsWindow controller = loader.getController();
            controller.showGroup(selected);
            this.showWindow(parent, "Study group details");
        } catch (IOException exception) {
            this.showError("Unable to open the group-details window.");
        }
    }

    private void showWindow(Parent parent, String title) {
        Stage window = new Stage();
        window.setTitle(title);
        window.setScene(new Scene(parent));
        window.initOwner(this.studyGroups.getScene().getWindow());
        window.initModality(Modality.WINDOW_MODAL);
        window.showAndWait();
    }

    @FXML
    void removeGroup() {
        StudyGroup selected = this.studyGroups.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.initOwner(this.studyGroups.getScene().getWindow());
        confirmation.setTitle("Delete study group");
        confirmation.setHeaderText("Delete " + selected.getName() + "?");
        confirmation.setContentText("This removes the group from this local session.");
        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            this.manager.deleteGroup(selected);
            this.refreshGroups();
        }
    }

    private void showError(String message) {
        Alert error = new Alert(AlertType.ERROR);
        error.initOwner(this.studyGroups.getScene().getWindow());
        error.setHeaderText("Unable to open window");
        error.setContentText(message);
        error.showAndWait();
    }
}
