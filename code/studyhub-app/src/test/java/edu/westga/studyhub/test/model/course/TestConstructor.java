package edu.westga.studyhub.test.model.course;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import edu.westga.studyhub.model.Course;

class TestConstructor {

    @Test
    void testNormalizesCodeAndTrimsTitle() {
        Course course = new Course("  cs101  ", "  Introduction to Programming  ");

        assertEquals("CS101", course.getCode());
        assertEquals("Introduction to Programming", course.getTitle());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void testRejectsMissingCode(String code) {
        assertThrows(IllegalArgumentException.class, () -> new Course(code, "Programming"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void testRejectsMissingTitle(String title) {
        assertThrows(IllegalArgumentException.class, () -> new Course("CS101", title));
    }

    @Test
    void testAcceptsSingleCharacterValues() {
        Course course = new Course("a", "B");

        assertEquals("A", course.getCode());
        assertEquals("B", course.getTitle());
    }

    @Test
    void testDisplaysCourseCodeAndTitle() {
        Course course = new Course("CS101", "Introduction to Programming");

        assertEquals("CS101 - Introduction to Programming", course.toString());
    }
}
