package edu.westga.studyhub.model;

import java.util.Locale;

/**
 * An immutable course identified by a normalized code and a display title.
 *
 * @author StudyHub contributors
 * @version Fall 2026
 */
public final class Course {

    private final String code;
    private final String title;

    /**
     * Create a course, trimming both values and uppercasing its code.
     *
     * @precondition code != null and not blank; title != null and not blank
     * @postcondition getCode() is the trimmed uppercase code and getTitle() is
     *                the trimmed title
     * @param code the course identifier, such as CS101
     * @param title the readable course title
     * @throws IllegalArgumentException if either value is null or blank
     */
    public Course(String code, String title) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Must provide a nonblank course code.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Must provide a nonblank course title.");
        }
        this.code = code.strip().toUpperCase(Locale.ROOT);
        this.title = title.strip();
    }

    /**
     * Return the normalized course identifier.
     *
     * @precondition none
     * @postcondition none
     * @return the nonblank uppercase code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Return the course's readable title.
     *
     * @precondition none
     * @postcondition none
     * @return the nonblank trimmed title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Describe the course for selection controls and lists.
     *
     * @precondition none
     * @postcondition none
     * @return the code followed by a separator and the title
     */
    @Override
    public String toString() {
        return this.code + " - " + this.title;
    }
}
