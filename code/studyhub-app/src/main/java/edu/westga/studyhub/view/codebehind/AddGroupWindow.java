package edu.westga.studyhub.view.codebehind;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.List;

import edu.westga.studyhub.model.StudyGroup;

/**
 * CodeBehind to handle processing for the AddGroupWindow.
 *
 * @author Moria El Akaya
 * @version Fall 2026
 */
public class AddGroupWindow {
    @FXML private AnchorPane guiPane;
    @FXML private TextField groupName;
    @FXML private TextField courseCode;
    @FXML private TextField capacity;

    private List<StudyGroup> studyGroups;

    private void closeWindow() {
        this.guiPane.getScene().getWindow().hide();
    }

    @FXML
    void addGroup(ActionEvent event) {
        try {
            String enteredName = this.groupName.getText();
            String enteredCourse = this.courseCode.getText();
            int enteredCapacity = Integer.parseInt(this.capacity.getText());
            StudyGroup group = new StudyGroup(enteredName, enteredCourse, enteredCapacity);
            this.studyGroups.add(group);
            this.closeWindow();
        } catch (NumberFormatException error) {
            Alert errorBox = new Alert(AlertType.ERROR);
            errorBox.setContentText("Must provide a valid number for capacity.");
            errorBox.showAndWait();
        } catch (IllegalArgumentException error) {
            Alert errorBox = new Alert(AlertType.ERROR);
            errorBox.setContentText(error.getMessage());
            errorBox.showAndWait();
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        this.closeWindow();
    }

    /**
     * Set the collection of study groups that newly created groups are added to.
     *
     * @precondition none
     * @postcondition none
     *
     * @param studyGroups the collection of study groups to add new groups to
     */
    public void setGroupList(List<StudyGroup> studyGroups) {
        this.studyGroups = studyGroups;
    }

    @FXML
    void initialize() {
        assert this.guiPane != null : "fx:id=\"guiPane\" was not injected: check your FXML file 'AddGroupWindow.fxml'.";
        assert this.groupName != null : "fx:id=\"groupName\" was not injected: check your FXML file 'AddGroupWindow.fxml'.";
        assert this.courseCode != null : "fx:id=\"courseCode\" was not injected: check your FXML file 'AddGroupWindow.fxml'.";
        assert this.capacity != null : "fx:id=\"capacity\" was not injected: check your FXML file 'AddGroupWindow.fxml'.";
    }
}
