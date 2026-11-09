package edu.westga.studyhub.model;

import java.util.Locale;

/**
 * Stores validated meeting information for a single study group on campus.
 *
 * @author Moria El Akaya
 * @version Fall 2026
 */
public final class StudyGroup {

    private static final String UNSPECIFIED = "Not specified";

    private final String name;
    private final String courseCode;
    private final int capacity;
    private final String description;
    private final String meetingTime;
    private final String location;

    /**
     * Create a basic group for callers that do not yet collect meeting details.
     * Meeting details default to "Not specified".
     *
     * @precondition name and courseCode are nonnull and nonblank; capacity &gt; 0
     * @postcondition text is stripped; course code is uppercase; capacity is stored
     * @param name the group name
     * @param courseCode the course code
     * @param capacity the maximum number of members
     * @throws IllegalArgumentException if text is null or blank or capacity is not positive
     */
    public StudyGroup(String name, String courseCode, int capacity) {
        this(name, courseCode, capacity, UNSPECIFIED, UNSPECIFIED, UNSPECIFIED);
    }

    /**
     * Create a group with a description, meeting time, and location.
     * Meeting time is descriptive text, not a parsed date or schedule.
     *
     * @precondition all text is nonnull and nonblank; capacity &gt; 0
     * @postcondition text is stripped; course code is uppercase; capacity is stored
     * @param name the group name
     * @param courseCode the course code, normalized using the root locale
     * @param capacity the positive maximum number of members
     * @param description the purpose or activities of the group
     * @param meetingTime the descriptive meeting time, such as Mondays at 3 PM
     * @param location the meeting place or online meeting information
     * @throws IllegalArgumentException if text is null or blank or capacity is not positive
     */
    public StudyGroup(String name, String courseCode, int capacity,
      String description, String meetingTime, String location) {
        this.name = StudyGroup.requireText(name, "Name");
        this.courseCode = StudyGroup.requireText(courseCode, "Course code").toUpperCase(Locale.ROOT);
        this.description = StudyGroup.requireText(description, "Description");
        this.meetingTime = StudyGroup.requireText(meetingTime, "Meeting time");
        this.location = StudyGroup.requireText(location, "Location");
        if (capacity <= 0) {
            throw new IllegalArgumentException("Must provide a positive value for capacity.");
        }
        this.capacity = capacity;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank.");
        }
        return value.strip();
    }

    /**
     * Return the name for the study group.
     *
     * @precondition none
     * @postcondition none
     *
     * @return the name for the study group
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return the course code for the study group.
     *
     * @precondition none
     * @postcondition none
     *
     * @return the course code for the study group
     */
    public String getCourseCode() {
        return this.courseCode;
    }

    /**
     * Return the maximum number of members for the study group.
     *
     * @precondition none
     * @postcondition none
     *
     * @return the maximum number of members for the study group
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Return the group's purpose or activities.
     *
     * @precondition none
     * @postcondition none
     * @return the stripped purpose or activities
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Return the group's descriptive meeting time.
     *
     * @precondition none
     * @postcondition none
     * @return the stripped descriptive meeting time
     */
    public String getMeetingTime() {
        return this.meetingTime;
    }

    /**
     * Return the group's meeting place or online meeting information.
     *
     * @precondition none
     * @postcondition none
     * @return the stripped meeting place or online meeting information
     */
    public String getLocation() {
        return this.location;
    }

    @Override
    public String toString() {
        return this.name + " [" + this.courseCode + "] seats: " + this.capacity;
    }
}
