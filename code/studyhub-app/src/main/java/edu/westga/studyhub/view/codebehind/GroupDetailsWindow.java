package edu.westga.studyhub.view.codebehind;

import edu.westga.studyhub.model.StudyGroup;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Displays the selected group's meeting information without editing it.
 *
 * @author Moria El Akaya
 * @version Fall 2026
 */
public class GroupDetailsWindow {

    @FXML private Label groupName;
    @FXML private Label courseCode;
    @FXML private Label capacity;
    @FXML private TextArea description;
    @FXML private Label meetingTime;
    @FXML private Label meetingLocation;

    /**
     * Populate the loaded details window from the selected group.
     *
     * @precondition group != null; the FXML controls have been injected
     * @postcondition every displayed field represents the supplied group
     * @param group the selected study group
     */
    public void showGroup(StudyGroup group) {
        this.groupName.setText(group.getName());
        this.courseCode.setText(group.getCourseCode());
        this.capacity.setText(Integer.toString(group.getCapacity()));
        this.description.setText(group.getDescription());
        this.meetingTime.setText(group.getMeetingTime());
        this.meetingLocation.setText(group.getLocation());
    }

    @FXML
    void close() {
        this.groupName.getScene().getWindow().hide();
    }
}
