package edu.westga.studyhub.model;

/**
 * Stores basic information for a single study group on campus.
 *
 * @author Moria El Akaya
 * @version Fall 2026
 */
public final class StudyGroup {

    private final String name;
    private final String courseCode;
    private final int capacity;

    /**
     * Create and initialize a new study group with the provided information.
     *
     * @precondition name != null &amp;&amp;
     *               !name.isEmpty() &amp;&amp;
     *               courseCode != null &amp;&amp;
     *               !courseCode.isEmpty() &amp;&amp;
     *               capacity &gt; 0
     * @postcondition getName() == name &amp;&amp;
     *                getCourseCode() == courseCode &amp;&amp;
     *                getCapacity() == capacity
     *
     * @param name the name for the new study group
     * @param courseCode the course the new study group meets for
     * @param capacity the maximum number of members for the new study group
     */
    public StudyGroup(String name, String courseCode, int capacity) {
        if (name == null) {
            throw new IllegalArgumentException("Must provide a name, but name was null.");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Must provide a name with at least one character.");
        }
        if (courseCode == null) {
            throw new IllegalArgumentException("Must provide a course code, but course code was null.");
        }
        if (courseCode.isEmpty()) {
            throw new IllegalArgumentException("Must provide a course code with at least one character.");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Must provide a positive value for capacity.");
        }
        this.name = name;
        this.courseCode = courseCode;
        this.capacity = capacity;
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

    @Override
    public String toString() {
        return this.name + " [" + this.courseCode + "] seats: " + this.capacity;
    }
}
