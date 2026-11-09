package edu.westga.studyhub.test.model.course_catalog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import edu.westga.studyhub.model.Course;
import edu.westga.studyhub.model.CourseCatalog;

class TestFindByCode {

    @Test
    void testFindsEveryCatalogCourse() {
        CourseCatalog catalog = new CourseCatalog();

        for (Course course : catalog.getCourses()) {
            assertSame(course, catalog.findByCode(course.getCode()).orElseThrow());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"CS101", "cs101", "  Cs101  ", "\tCS101\n"})
    void testMatchesIgnoringCaseAndSurroundingWhitespace(String code) {
        CourseCatalog catalog = new CourseCatalog();

        assertEquals("CS101", catalog.findByCode(code).orElseThrow().getCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "CS", "CS999", "CS101-extra", "CS 101"})
    void testDoesNotFindBlankPartialOrUnknownCodes(String code) {
        CourseCatalog catalog = new CourseCatalog();

        assertTrue(catalog.findByCode(code).isEmpty());
    }

    @Test
    void testRejectsNullCode() {
        CourseCatalog catalog = new CourseCatalog();

        assertThrows(IllegalArgumentException.class, () -> catalog.findByCode(null));
    }
}
