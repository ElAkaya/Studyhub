package edu.westga.studyhub.test.model.study_group;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import edu.westga.studyhub.model.StudyGroup;

class TestConstructor {

    @ParameterizedTest
    @CsvSource({"Morning Crew, CS3230, 5", "Late Night Lab, DEVOPS4420, 12"})
    void testValidInput(String name, String courseCode, int capacity) {
        StudyGroup result = new StudyGroup(name, courseCode, capacity);

        assertEquals(name, result.getName(), "checking name of study group");
        assertEquals(courseCode, result.getCourseCode(), "checking course code of study group");
        assertEquals(capacity, result.getCapacity(), "checking capacity of study group");
    }

    @ParameterizedTest
    @CsvSource({", CS3230, 5", "'', CS3230, 5", "Morning Crew, , 5", "Morning Crew, '', 5", "Morning Crew, CS3230, 0", "Morning Crew, CS3230, -1"})
    void testInvalidInput(String name, String courseCode, int capacity) {
        assertThrows(IllegalArgumentException.class, () -> {
            new StudyGroup(name, courseCode, capacity);
        });
    }
}
