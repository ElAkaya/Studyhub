package edu.westga.studyhub.view.codebehind;

import edu.westga.studyhub.model.Course;
import edu.westga.studyhub.model.GroupManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Reads the creation form and displays validation errors from GroupManager.
 * Invalid input stays in the form so the student can correct it.
 *
 * @author Moria El Akaya
 * @version Fall 2026
 */
public class AddGroupWindow {

    @FXML private TextField groupName;
    @FXML private ComboBox<Course> courseSelector;
    @FXML private TextField capacity;
    @FXML private TextArea description;
    @FXML private TextField meetingTime;
    @FXML private TextField meetingLocation;
    @FXML private Label errorMessage;

    private GroupManager manager;

    /**
     * Connect the form to the application's shared manager and course selection.
     * Call after loading FXML and before showing the window.
     *
     * @precondition manager != null; the FXML controls have been injected
     * @postcondition courses are displayed and the current course is selected
     * @param manager the manager used by the main window
     * @param selectedCourse the main window's selected course, or null for no selection
     */
    public void configure(GroupManager manager, Course selectedCourse) {
        this.manager = manager;
        this.courseSelector.getItems().setAll(manager.getCourses());
        this.courseSelector.setValue(selectedCourse);
    }

    @FXML
    void addGroup() {
        Course selectedCourse = this.courseSelector.getValue();
        String code = selectedCourse == null ? null : selectedCourse.getCode();
        try {
            this.manager.createGroup(this.groupName.getText(), code, this.capacity.getText(),
                this.description.getText(), this.meetingTime.getText(), this.meetingLocation.getText());
            this.closeWindow();
        } catch (IllegalArgumentException exception) {
            this.errorMessage.setText(exception.getMessage());
        }
    }

    @FXML
    void cancel() {
        this.closeWindow();
    }

    private void closeWindow() {
        this.groupName.getScene().getWindow().hide();
    }
}
