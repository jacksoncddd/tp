package seedu.address.model.task;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents a list of maintenance tasks.
 */
public class MaintenanceTaskList {
    private final ObservableList<MaintenanceTask> tasks = FXCollections.observableArrayList();
    private final ObservableList<MaintenanceTask> unmodifiableTasks = FXCollections.unmodifiableObservableList(tasks);

    public void addTask(MaintenanceTask task) {
        tasks.add(task);
    }

    /** Removes all tasks from the list. */
    public void clearTasks() {
        tasks.clear();
    }

    /**
     * Removes the task at the specified index from the list.
     * @param index The 0-based index of the task to remove.
     */
    public void removeTask(int index) {
        assert index >= 0 : "Index should not be negative";
        assert index < tasks.size() : "Index should be within list size";
        tasks.remove(index);
    }

    /**
     * Removes the given task from the list by reference.
     * @param task The task to remove.
     */
    public void removeTask(MaintenanceTask task) {
        tasks.remove(task);
    }

    public List<MaintenanceTask> getTasks() {
        return tasks;
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public int size() {
        return tasks.size();
    }

    /**
     * Returns true if a task with the same facility and date already exists in the
     * list.
     *
     * @param facility The facility name to check.
     * @param date     The date to check.
     * @return {@code true} if a duplicate exists, {@code false} otherwise.
     */
    public boolean hasDuplicate(String facility, LocalDate date) {
        return tasks.stream()
                .anyMatch(t -> t.getFacility().equalsIgnoreCase(facility)
                        && t.getDate().equals(date));
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<MaintenanceTask> asUnmodifiableObservableList() {
        return unmodifiableTasks;
    }

    /**
     * Sorts tasks in-place by date (ascending). Ties are broken deterministically
     * to ensure a stable order.
     */
    public void sortTasksByDate() {
        tasks.sort(Comparator
                .comparing(MaintenanceTask::getDate)
                .thenComparing(MaintenanceTask::getFacility, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(t -> t.getContractorName().fullName, String.CASE_INSENSITIVE_ORDER));
    }
}
