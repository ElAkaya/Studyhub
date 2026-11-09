package edu.westga.studyhub.test.model.study_group;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import edu.westga.studyhub.model.StudyGroup;

class TestMeetingDetails {

    @Test
    void testStoresAndNormalizesAllDetails() {
        StudyGroup group = new StudyGroup("  Exam prep  ", " cs101 ", 1,
            "  Practice problems  ", " Monday at 3 PM ", " Library room 2 ");
        assertEquals("Exam prep", group.getName());
        assertEquals("CS101", group.getCourseCode());
        assertEquals(1, group.getCapacity());
        assertEquals("Practice problems", group.getDescription());
        assertEquals("Monday at 3 PM", group.getMeetingTime());
        assertEquals("Library room 2", group.getLocation());
        assertEquals("Exam prep [CS101] seats: 1", group.toString());
    }

    @Test
    void testBasicConstructorKeepsUnspecifiedMeetingDetails() {
        StudyGroup group = new StudyGroup(" Group ", " cs101 ", 5);
        assertEquals("Group", group.getName());
        assertEquals("CS101", group.getCourseCode());
        assertEquals("Not specified", group.getDescription());
        assertEquals("Not specified", group.getMeetingTime());
        assertEquals("Not specified", group.getLocation());
    }

    @Test
    void testMaximumPositiveCapacity() {
        StudyGroup group = new StudyGroup("Group", "CS101", Integer.MAX_VALUE,
            "Practice", "Friday", "Online");
        assertEquals(Integer.MAX_VALUE, group.getCapacity());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, Integer.MIN_VALUE})
    void testRejectsNonpositiveCapacity(int capacity) {
        assertThrows(IllegalArgumentException.class,
            () -> new StudyGroup("Group", "CS101", capacity, "Practice", "Friday", "Online"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\n", "\u2003"})
    void testRejectsInvalidName(String value) {
        assertThrows(IllegalArgumentException.class,
            () -> new StudyGroup(value, "CS101", 5, "Practice", "Friday", "Online"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\n", "\u2003"})
    void testRejectsInvalidCourseCode(String value) {
        assertThrows(IllegalArgumentException.class,
            () -> new StudyGroup("Group", value, 5, "Practice", "Friday", "Online"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\n", "\u2003"})
    void testRejectsInvalidDescription(String value) {
        assertThrows(IllegalArgumentException.class,
            () -> new StudyGroup("Group", "CS101", 5, value, "Friday", "Online"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\n", "\u2003"})
    void testRejectsInvalidMeetingTime(String value) {
        assertThrows(IllegalArgumentException.class,
            () -> new StudyGroup("Group", "CS101", 5, "Practice", value, "Online"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\n", "\u2003"})
    void testRejectsInvalidLocation(String value) {
        assertThrows(IllegalArgumentException.class,
            () -> new StudyGroup("Group", "CS101", 5, "Practice", "Friday", value));
    }
}
