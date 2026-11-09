package edu.westga.studyhub.test.model.group_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import edu.westga.studyhub.model.CourseCatalog;
import edu.westga.studyhub.model.GroupManager;
import edu.westga.studyhub.model.StudyGroup;

class TestGroupManager {

    private StudyGroup create(GroupManager manager, String code) {
        return manager.createGroup("Practice", code, "5", "Review exercises", "Monday at 3 PM", "Library");
    }

    @Test
    void testStartsEmptyWithCatalogCourses() {
        CourseCatalog catalog = new CourseCatalog();
        GroupManager manager = new GroupManager(catalog);
        assertTrue(manager.getGroups().isEmpty());
        assertSame(catalog.getCourses(), manager.getCourses());
        assertEquals(4, new GroupManager().getCourses().size());
        assertThrows(UnsupportedOperationException.class, () -> manager.getCourses().clear());
    }

    @Test
    void testRejectsNullCatalog() {
        assertThrows(IllegalArgumentException.class, () -> new GroupManager(null));
    }

    @Test
    void testCreatesNormalizedGroupAndPreservesOrder() {
        GroupManager manager = new GroupManager();
        StudyGroup first = manager.createGroup("  Review  ", " cs101 ", " 1 ",
            " Problems ", " Tuesday ", " Online ");
        StudyGroup second = this.create(manager, "CS201");
        assertEquals(List.of(first, second), manager.getGroups());
        assertEquals("Review", first.getName());
        assertEquals("CS101", first.getCourseCode());
        assertEquals(1, first.getCapacity());
        assertEquals("Problems", first.getDescription());
        assertEquals("Tuesday", first.getMeetingTime());
        assertEquals("Online", first.getLocation());
    }

    @Test
    void testSnapshotCannotChangeManagerAndDoesNotChangeLater() {
        GroupManager manager = new GroupManager();
        StudyGroup first = this.create(manager, "CS101");
        List<StudyGroup> snapshot = manager.getGroups();
        assertThrows(UnsupportedOperationException.class, () -> snapshot.clear());
        this.create(manager, "CS201");
        assertEquals(List.of(first), snapshot);
        assertEquals(2, manager.getGroups().size());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "2.5", "words", "2147483648", "0", "-1", "1 2"})
    void testInvalidCapacityLeavesCollectionUnchanged(String capacity) {
        GroupManager manager = new GroupManager();
        StudyGroup existing = this.create(manager, "CS101");
        assertThrows(IllegalArgumentException.class,
            () -> manager.createGroup("New", "CS201", capacity, "Review", "Friday", "Room 1"));
        assertEquals(List.of(existing), manager.getGroups());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "CS", "UNKNOWN"})
    void testInvalidCourseLeavesCollectionUnchanged(String code) {
        GroupManager manager = new GroupManager();
        assertThrows(IllegalArgumentException.class, () -> this.create(manager, code));
        assertTrue(manager.getGroups().isEmpty());
    }

    @Test
    void testInvalidMeetingTextLeavesCollectionUnchanged() {
        GroupManager manager = new GroupManager();
        assertThrows(IllegalArgumentException.class,
            () -> manager.createGroup(" ", "CS101", "5", "Review", "Friday", "Room 1"));
        assertThrows(IllegalArgumentException.class,
            () -> manager.createGroup("Group", "CS101", "5", null, "Friday", "Room 1"));
        assertThrows(IllegalArgumentException.class,
            () -> manager.createGroup("Group", "CS101", "5", "Review", "", "Room 1"));
        assertThrows(IllegalArgumentException.class,
            () -> manager.createGroup("Group", "CS101", "5", "Review", "Friday", " "));
        assertTrue(manager.getGroups().isEmpty());
    }

    @Test
    void testAllCoursesAndBlankSearch() {
        GroupManager manager = new GroupManager();
        StudyGroup first = this.create(manager, "CS101");
        StudyGroup second = this.create(manager, "MATH101");
        assertEquals(List.of(first, second), manager.findGroups(null, " "));
        assertEquals(List.of(first), manager.findGroups(" cs101 ", ""));
    }

    @Test
    void testSearchIgnoresCaseAndWhitespaceAndAllowsPartialCode() {
        GroupManager manager = new GroupManager();
        StudyGroup first = this.create(manager, "CS101");
        StudyGroup second = this.create(manager, "CS201");
        this.create(manager, "ENG101");
        assertEquals(List.of(first, second), manager.findGroups(null, " cs "));
        assertEquals(List.of(first), manager.findGroups(null, "cs101"));
        assertTrue(manager.findGroups(null, "unknown").isEmpty());
    }

    @Test
    void testSelectionAndSearchCombine() {
        GroupManager manager = new GroupManager();
        StudyGroup first = this.create(manager, "CS101");
        this.create(manager, "CS201");
        assertEquals(List.of(first), manager.findGroups("CS101", "cs"));
        assertTrue(manager.findGroups("CS101", "201").isEmpty());
        assertTrue(manager.findGroups("ENG101", "").isEmpty());
        assertTrue(manager.findGroups(null, "Practice").isEmpty());
    }

    @Test
    void testSearchResultIsUnmodifiableSnapshot() {
        GroupManager manager = new GroupManager();
        StudyGroup first = this.create(manager, "CS101");
        List<StudyGroup> results = manager.findGroups(null, "CS");
        assertThrows(UnsupportedOperationException.class, () -> results.clear());
        this.create(manager, "CS201");
        assertEquals(List.of(first), results);
        assertEquals(2, manager.getGroups().size());
    }

    @Test
    void testRejectsNullSearch() {
        GroupManager manager = new GroupManager();
        assertThrows(IllegalArgumentException.class, () -> manager.findGroups(null, null));
    }

    @Test
    void testEmptyCollectionProducesEmptyResults() {
        GroupManager manager = new GroupManager();
        assertTrue(manager.findGroups(null, "").isEmpty());
        assertTrue(manager.findGroups("CS101", "CS").isEmpty());
    }

    @Test
    void testDeletesOnlyExactGroupAndRepeatedDeletionReturnsFalse() {
        GroupManager manager = new GroupManager();
        StudyGroup first = this.create(manager, "CS101");
        StudyGroup second = this.create(manager, "CS101");
        assertTrue(manager.deleteGroup(first));
        assertEquals(List.of(second), manager.getGroups());
        assertFalse(manager.deleteGroup(first));
        assertFalse(manager.deleteGroup(new StudyGroup("Practice", "CS101", 5)));
        assertEquals(List.of(second), manager.findGroups(null, ""));
    }

    @Test
    void testRejectsDeletionWithoutSelection() {
        GroupManager manager = new GroupManager();
        StudyGroup existing = this.create(manager, "CS101");
        assertThrows(IllegalArgumentException.class, () -> manager.deleteGroup(null));
        assertEquals(List.of(existing), manager.getGroups());
    }
}
