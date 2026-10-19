package edu.westga.studyhub.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Manages local study groups and course-code searches for one application session.
 * Data is kept in memory and is lost when the application closes.
 *
 * @author Moria El Akaya
 * @version Fall 2026
 */
public final class GroupManager {

    private final CourseCatalog catalog;
    private final List<StudyGroup> groups;

    /**
     * Create an empty group collection using the demonstration course catalog.
     *
     * @precondition none
     * @postcondition getGroups().isEmpty()
     */
    public GroupManager() {
        this(new CourseCatalog());
    }

    /**
     * Create an empty collection for the provided catalog.
     *
     * @precondition catalog != null
     * @postcondition getGroups().isEmpty(); getCourses() uses the supplied catalog
     * @param catalog the courses available when creating groups
     * @throws IllegalArgumentException if catalog is null
     */
    public GroupManager(CourseCatalog catalog) {
        if (catalog == null) {
            throw new IllegalArgumentException("Must provide a course catalog.");
        }
        this.catalog = catalog;
        this.groups = new ArrayList<>();
    }

    /**
     * Return the courses available for group creation and selection.
     *
     * @precondition none
     * @postcondition none
     * @return an unmodifiable list of catalog courses
     */
    public List<Course> getCourses() {
        return this.catalog.getCourses();
    }

    /**
     * Return a snapshot of all groups in creation order.
     *
     * @precondition none
     * @postcondition changes to the manager cannot change the returned snapshot
     * @return an unmodifiable snapshot of the current groups
     */
    public List<StudyGroup> getGroups() {
        return List.copyOf(this.groups);
    }

    /**
     * Validate the form and create a group only when every value is valid.
     * Codes match a complete catalog code, ignoring case and surrounding spaces.
     *
     * @precondition text is nonnull and nonblank; code is in the catalog;
     *               capacity is a positive integer within the int range
     * @postcondition a valid group is appended; invalid input leaves groups unchanged
     * @param name the group name
     * @param courseCode the selected catalog course code
     * @param capacity the maximum members entered as text
     * @param description the group's purpose
     * @param meetingTime the descriptive meeting time
     * @param location the meeting place or online meeting information
     * @return the newly created group
     * @throws IllegalArgumentException if any input is invalid
     */
    public StudyGroup createGroup(String name, String courseCode, String capacity,
      String description, String meetingTime, String location) {
        Course course = this.catalog.findByCode(courseCode)
            .orElseThrow(() -> new IllegalArgumentException("Select a course from the catalog."));
        int maximumMembers = GroupManager.parseCapacity(capacity);
        StudyGroup group = new StudyGroup(name, course.getCode(), maximumMembers,
            description, meetingTime, location);
        this.groups.add(group);
        return group;
    }

    private static int parseCapacity(String capacity) {
        if (capacity == null || capacity.isBlank()) {
            throw new IllegalArgumentException("Capacity must be a positive whole number.");
        }
        try {
            return Integer.parseInt(capacity.strip());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Capacity must be a positive whole number within the integer range.",
                exception);
        }
    }

    /**
     * Find groups using both the selected course and a partial course-code search.
     * Null selection means all courses. Blank search shows every group permitted
     * by the selection. Search ignores case and surrounding whitespace.
     *
     * @precondition searchText != null
     * @postcondition the stored groups are unchanged
     * @param selectedCourseCode the complete course code, or null for all courses
     * @param searchText the partial course code to match
     * @return an unmodifiable result in creation order
     * @throws IllegalArgumentException if searchText is null
     */
    public List<StudyGroup> findGroups(String selectedCourseCode, String searchText) {
        if (searchText == null) {
            throw new IllegalArgumentException("Must provide search text; use an empty string to show all.");
        }
        String query = searchText.strip().toUpperCase(Locale.ROOT);
        String selectedCode = selectedCourseCode == null ? null : selectedCourseCode.strip();
        return this.groups.stream()
            .filter(group -> selectedCode == null || group.getCourseCode().equalsIgnoreCase(selectedCode))
            .filter(group -> group.getCourseCode().contains(query))
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Remove the exact selected group after the caller obtains confirmation.
     * Identical-looking groups remain separate objects and are removed individually.
     *
     * @precondition group != null
     * @postcondition the selected group is removed if present; other groups remain
     * @param group the group selected for deletion
     * @return true if the group was removed, false if it was not in the collection
     * @throws IllegalArgumentException if no group was selected
     */
    public boolean deleteGroup(StudyGroup group) {
        if (group == null) {
            throw new IllegalArgumentException("Select a group to delete.");
        }
        return this.groups.remove(group);
    }
}
