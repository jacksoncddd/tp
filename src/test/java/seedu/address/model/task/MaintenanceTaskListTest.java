package seedu.address.model.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.person.Name;
import seedu.address.model.person.Service;
import seedu.address.model.tag.Tag;

/**
 * Contains unit tests for MaintenanceTaskList.
 */
public class MaintenanceTaskListTest {

    private MaintenanceTaskList taskList;
    private MaintenanceTask sampleTask;

    @BeforeEach
    public void setUp() {
        taskList = new MaintenanceTaskList();
        sampleTask = new MaintenanceTask(
                "Sports Hall",
                LocalDate.of(2026, 12, 1),
                new Name("Alice Pauline"),
                Set.of(new Tag("electrician")),
                new Service("Electrician"));
    }

    @Test
    public void isEmpty_emptyList_returnsTrue() {
        assertTrue(taskList.isEmpty());
    }

    @Test
    public void isEmpty_nonEmptyList_returnsFalse() {
        taskList.addTask(sampleTask);
        assertFalse(taskList.isEmpty());
    }

    @Test
    public void addTask_validTask_success() {
        taskList.addTask(sampleTask);
        assertEquals(1, taskList.size());
    }

    @Test
    public void removeTask_validIndex_success() {
        taskList.addTask(sampleTask);
        taskList.removeTask(0);
        assertTrue(taskList.isEmpty());
    }

    @Test
    public void hasDuplicate_duplicateTask_returnsTrue() {
        taskList.addTask(sampleTask);
        assertTrue(taskList.hasDuplicate("Sports Hall", LocalDate.of(2026, 12, 1)));
    }

    @Test
    public void hasDuplicate_noDuplicate_returnsFalse() {
        taskList.addTask(sampleTask);
        assertFalse(taskList.hasDuplicate("Swimming Pool", LocalDate.of(2026, 12, 1)));
    }

    @Test
    public void size_multipleTasksAdded_returnsCorrectSize() {
        taskList.addTask(sampleTask);
        MaintenanceTask anotherTask = new MaintenanceTask(
                "Swimming Pool",
                LocalDate.of(2026, 12, 5),
                new Name("Benson Meier"),
                Set.of(new Tag("plumber")),
                new Service("Plumber"));
        taskList.addTask(anotherTask);
        assertEquals(2, taskList.size());
    }

    @Test
    public void sortTasksByDate_tasksSortedByAscendingDate() {
        MaintenanceTask laterTask = new MaintenanceTask(
                "Swimming Pool",
                LocalDate.of(2026, 12, 15),
                new Name("Benson Meier"),
                Set.of(new Tag("plumber")),
                new Service("Plumber"));
        MaintenanceTask earlierTask = new MaintenanceTask(
                "Sports Hall",
                LocalDate.of(2026, 12, 1),
                new Name("Alice Pauline"),
                Set.of(new Tag("electrician")),
                new Service("Electrician"));

        // Add in reverse order to verify sorting actually changes the order.
        taskList.addTask(laterTask);
        taskList.addTask(earlierTask);

        taskList.sortTasksByDate();

        assertEquals(2, taskList.size());
        assertEquals("Sports Hall", taskList.getTasks().get(0).getFacility());
        assertEquals("Swimming Pool", taskList.getTasks().get(1).getFacility());
    }
}
