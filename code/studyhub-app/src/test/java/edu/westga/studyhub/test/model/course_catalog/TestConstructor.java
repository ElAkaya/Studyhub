package edu.westga.studyhub.test.model.course_catalog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import edu.westga.studyhub.model.Course;
import edu.westga.studyhub.model.CourseCatalog;

class TestConstructor {

    @Test
    void testProvidesDistinctDemonstrationCoursesInOrder() {
        CourseCatalog catalog = new CourseCatalog();
        List<String> codes = catalog.getCourses().stream().map(Course::getCode).collect(Collectors.toList());

        assertEquals(List.of("CS101", "CS201", "MATH101", "ENG101"), codes);
        assertEquals(4, codes.stream().distinct().count());
    }

    @Test
    void testCannotAddToCatalogThroughReturnedList() {
        CourseCatalog catalog = new CourseCatalog();

        assertThrows(UnsupportedOperationException.class,
            () -> catalog.getCourses().add(new Course("CS999", "Other")));
        assertEquals(4, catalog.getCourses().size());
    }

    @Test
    void testCannotRemoveCoursesThroughReturnedList() {
        CourseCatalog catalog = new CourseCatalog();

        assertThrows(UnsupportedOperationException.class, () -> catalog.getCourses().clear());
        assertEquals(4, catalog.getCourses().size());
    }
}
