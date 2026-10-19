package edu.westga.studyhub.test.model.study_group;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.westga.studyhub.model.StudyGroup;

class TestToString {

    @Test
    void testDisplaysNameCourseAndCapacity() {
        StudyGroup group = new StudyGroup("Morning Crew", "CS3230", 5);

        assertEquals("Morning Crew [CS3230] seats: 5", group.toString(), "checking string representation of study group");
    }
}
