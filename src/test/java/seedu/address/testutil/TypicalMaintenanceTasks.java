package seedu.address.testutil;

import java.time.LocalDate;
import java.util.Set;

import seedu.address.model.person.Name;
import seedu.address.model.person.Service;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.MaintenanceTask;
import seedu.address.model.task.MaintenanceTaskList;

/**
 * A utility class containing a list of {@code MaintenanceTask} objects to be
 * used in tests.
 */
public class TypicalMaintenanceTasks {

    public static final MaintenanceTask SPORTS_HALL = new MaintenanceTask(
            "Sports Hall",
            LocalDate.of(2026, 12, 1),
            new Name("Alice Pauline"),
            Set.of(new Tag("electrician")),
            new Service("Electrician"));

    public static final MaintenanceTask SWIMMING_POOL = new MaintenanceTask(
            "Swimming Pool",
            LocalDate.of(2026, 12, 15),
            new Name("Benson Meier"),
            Set.of(new Tag("plumber")),
            new Service("Plumber"));

    /** Prevents instantiation. */
    private TypicalMaintenanceTasks() {
    }

    /**
     * Returns a {@code MaintenanceTaskList} containing all typical tasks.
     */
    public static MaintenanceTaskList getTypicalTaskList() {
        MaintenanceTaskList taskList = new MaintenanceTaskList();
        taskList.addTask(SPORTS_HALL);
        taskList.addTask(SWIMMING_POOL);
        return taskList;
    }
}
