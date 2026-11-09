package edu.westga.studyhub.model;

import java.util.List;
import java.util.Optional;

/**
 * A fixed demonstration semester catalog, not an official university listing.
 * The catalog and its courses cannot be changed by callers.
 *
 * @author StudyHub contributors
 * @version Fall 2026
 */
public final class CourseCatalog {

    private final List<Course> courses;

    /**
     * Create the four example courses available in the local demonstration.
     *
     * @precondition none
     * @postcondition getCourses() contains four distinct courses in display order
     */
    public CourseCatalog() {
        this.courses = List.of(
            new Course("CS101", "Introduction to Programming"),
            new Course("CS201", "Data Structures"),
            new Course("MATH101", "College Algebra"),
            new Course("ENG101", "College Writing"));
    }

    /**
     * Return the demonstration semester's courses in display order.
     *
     * @precondition none
     * @postcondition none
     * @return an unmodifiable list of immutable courses
     */
    public List<Course> getCourses() {
        return this.courses;
    }

    /**
     * Look up a complete course code, ignoring case and surrounding whitespace.
     * Blank, partial and unknown codes produce an empty result.
     *
     * @precondition code != null
     * @postcondition the catalog is unchanged
     * @param code the complete code to locate
     * @return the catalog course if found, otherwise an empty optional
     * @throws IllegalArgumentException if code is null
     */
    public Optional<Course> findByCode(String code) {
        if (code == null) {
            throw new IllegalArgumentException("Must provide a course code to look up.");
        }
        String normalizedCode = code.strip();
        return this.courses.stream()
            .filter(course -> course.getCode().equalsIgnoreCase(normalizedCode))
            .findFirst();
    }
}
