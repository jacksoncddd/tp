package seedu.address.model.task;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Name;
import seedu.address.model.person.Service;
import seedu.address.model.tag.Tag;


/**
 * Represents a maintenance task in the task list.
 * Guarantees: facility and date are non-null and validated at construction.
 */
public class MaintenanceTask {

    public static final int MAX_FACILITY_LENGTH = 50;

    private final String facility;
    private final LocalDate date;
    //private final int contractorIndex;
    private final Name contractorName;
    private final Set<Tag> tags = new HashSet<>();
    private final Service contractorService;
    private boolean isCompleted;


    /**
     * Constructs a {@code MaintenanceTask}.
     *
     * @param facility       The name of the facility (1–50 chars).
     * @param date           The scheduled date.
     * @param contractorName The name of the contractor responsible for the task.
     * @param tags           The set of tags associated with the task.
     * @param service        The service provided by the contractor for this task.
     */
    public MaintenanceTask(String facility, LocalDate date, Name contractorName, Set<Tag> tags, Service service) {
        assert facility != null : "Facility should not be null";
        assert !facility.isBlank() : "Facility should not be blank";
        assert date != null : "Date should not be null";
        assert contractorName != null : "Contractor name should not be null";
        assert tags != null : "Tags should not be null";
        assert service != null : "Service should not be null";
        this.facility = facility;
        this.date = date;
        this.contractorName = contractorName;
        this.tags.addAll(tags);
        this.contractorService = service;
        this.isCompleted = false;
    }

    /**
     * Returns the contractor's service for this maintenance task.
     * @return The contractor's service.
     */
    public Service getContractorService() {
        return this.contractorService;
    }

    /** Returns the facility name. */
    public String getFacility() {
        return facility;
    }

    /** Returns the scheduled date. */
    public LocalDate getDate() {
        return date;
    }

    public Name getContractorName() {
        return contractorName;
    }

    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Marks this maintenance task as completed. Once marked, it cannot be undone.
     */
    public void markAsCompleted() {
        assert !isCompleted : "Task should not already be completed";
        this.isCompleted = true;
    }

    @Override
    public String toString() {
        String tagsString = tags.stream()
                .map(tag -> tag.tagName)
                .collect(java.util.stream.Collectors.joining(", "));

        return facility + " on " + date
                + " (Contractor: " + contractorName + " [" + tagsString + "])";
    }

    /**
     * Returns true if both maintenance tasks have the same facility, date,
     * contractor name, and tags.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MaintenanceTask)) {
            return false;
        }

        MaintenanceTask otherTask = (MaintenanceTask) other;
        return facility.equals(otherTask.facility)
                && date.equals(otherTask.date)
                && contractorName.equals(otherTask.contractorName)
                && tags.equals(otherTask.tags);
    }

    /**
     * Unmarks this maintenance task as completed.
     */
    public void unmarkAsCompleted() {
        assert isCompleted : "Task should already be completed before unmarking";
        this.isCompleted = false;
    }

    /**
     * Returns the hash code of this maintenance task based on its identity fields.
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(facility, date, contractorName, tags);
    }

}
