package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Service;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.MaintenanceTask;

/**
 * Edits the details of an existing maintenance task.
 */
public class EdittCommand extends Command {

    public static final String COMMAND_WORD = "editt";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the maintenance task identified "
            + "by the index number used in the displayed task list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[f/FACILITY] "
            + "[d/DATE (YYYY-MM-DD)] "
            + "[c/CONTRACTOR_INDEX]\n"
            + "Example: " + COMMAND_WORD + " 1 f/Sports Hall d/2026-12-20";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited Maintenance Task: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TASK = "A task for this facility on the same date already exists.";
    public static final String MESSAGE_INVALID_CONTRACTOR_INDEX = "The contractor index provided is invalid.";
    public static final String MESSAGE_CANNOT_EDIT_COMPLETED_TASK =
            "Cannot edit a completed task. Completed tasks are kept for reporting.";

    private final Index targetIndex;
    private final EditTaskDescriptor editTaskDescriptor;

    /**
     * Creates an {@code EdittCommand}.
     *
     * @param targetIndex index of the task in the displayed task list.
     * @param editTaskDescriptor details to edit the task with.
     */
    public EdittCommand(Index targetIndex, EditTaskDescriptor editTaskDescriptor) {
        requireNonNull(targetIndex);
        requireNonNull(editTaskDescriptor);
        this.targetIndex = targetIndex;
        this.editTaskDescriptor = new EditTaskDescriptor(editTaskDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<MaintenanceTask> taskList = model.getMaintenanceTaskList().getTasks();

        if (targetIndex.getZeroBased() >= taskList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_INDEX);
        }

        MaintenanceTask taskToEdit = taskList.get(targetIndex.getZeroBased());
        if (taskToEdit.isCompleted()) {
            throw new CommandException(MESSAGE_CANNOT_EDIT_COMPLETED_TASK);
        }

        int updatedContractorFullIndex = resolveUpdatedContractorFullIndex(model, taskToEdit);
        Person updatedContractor = resolvePersonByFullIndex(model, updatedContractorFullIndex);
        MaintenanceTask editedTask = createEditedTask(taskToEdit, editTaskDescriptor,
                updatedContractorFullIndex, updatedContractor);

        if (isDuplicateTask(taskList, editedTask, targetIndex.getZeroBased())) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }

        taskList.set(targetIndex.getZeroBased(), editedTask);

        String tagsString = editedTask.getTags().stream()
                .map(tag -> tag.tagName)
                .collect(Collectors.joining(", "));
        String taskDisplay = editedTask.getFacility() + " on " + editedTask.getDate()
                + " (Contractor: " + updatedContractor.getName().fullName
                + " | Service: " + editedTask.getContractorService()
                + " | Tags: [" + tagsString + "])";
        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, taskDisplay));
    }

    /**
     * Resolves the contractor full-list index after applying descriptor changes.
     */
    private int resolveUpdatedContractorFullIndex(Model model, MaintenanceTask taskToEdit) throws CommandException {
        if (editTaskDescriptor.getContractorIndex().isEmpty()) {
            return taskToEdit.getContractorIndex();
        }

        Index updatedContractorFilteredIndex = editTaskDescriptor.getContractorIndex().get();
        int zeroBasedIndex = updatedContractorFilteredIndex.getZeroBased();
        if (zeroBasedIndex >= model.getFilteredPersonList().size()) {
            throw new CommandException(MESSAGE_INVALID_CONTRACTOR_INDEX);
        }

        Person contractor = model.getFilteredPersonList().get(zeroBasedIndex);
        int fullListIndex = model.getAddressBook().getPersonList().indexOf(contractor) + 1;
        if (fullListIndex <= 0) {
            throw new CommandException(MESSAGE_INVALID_CONTRACTOR_INDEX);
        }

        return fullListIndex;
    }

    /**
     * Resolves a contractor from the full person list using a 1-based full list index.
     */
    private Person resolvePersonByFullIndex(Model model, int contractorFullIndex) throws CommandException {
        int zeroBased = contractorFullIndex - 1;
        List<Person> allPersons = model.getAddressBook().getPersonList();
        if (zeroBased < 0 || zeroBased >= allPersons.size()) {
            throw new CommandException("Contractor linked to this task no longer exists.");
        }
        return allPersons.get(zeroBased);
    }

    /**
     * Creates a new {@code MaintenanceTask} by applying descriptor changes over {@code taskToEdit}.
     */
    private static MaintenanceTask createEditedTask(MaintenanceTask taskToEdit, EditTaskDescriptor descriptor,
                                                    int contractorFullIndex, Person contractor) {
        String updatedFacility = descriptor.getFacility().orElse(taskToEdit.getFacility());
        LocalDate updatedDate = descriptor.getDate().orElse(taskToEdit.getDate());
        Set<Tag> updatedTags = contractor.getTags();
        Service updatedService = contractor.getService();

        MaintenanceTask editedTask = new MaintenanceTask(updatedFacility, updatedDate,
                contractorFullIndex, updatedTags, updatedService);

        if (taskToEdit.isCompleted()) {
            editedTask.markAsCompleted();
        }
        return editedTask;
    }

    /**
     * Returns true if a task with the same facility and date exists, excluding the edited task itself.
     */
    private static boolean isDuplicateTask(List<MaintenanceTask> taskList, MaintenanceTask candidate,
                                           int excludedIndex) {
        for (int i = 0; i < taskList.size(); i++) {
            if (i == excludedIndex) {
                continue;
            }
            MaintenanceTask existingTask = taskList.get(i);
            if (existingTask.getFacility().equalsIgnoreCase(candidate.getFacility())
                    && existingTask.getDate().equals(candidate.getDate())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EdittCommand)) {
            return false;
        }
        EdittCommand otherCommand = (EdittCommand) other;
        return targetIndex.equals(otherCommand.targetIndex)
                && editTaskDescriptor.equals(otherCommand.editTaskDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("editTaskDescriptor", editTaskDescriptor)
                .toString();
    }

    /**
     * Stores task fields to edit.
     */
    public static class EditTaskDescriptor {
        private String facility;
        private LocalDate date;
        private Index contractorIndex;

        /**
         * Creates an empty {@code EditTaskDescriptor}.
         */
        public EditTaskDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditTaskDescriptor(EditTaskDescriptor toCopy) {
            setFacility(toCopy.facility);
            setDate(toCopy.date);
            setContractorIndex(toCopy.contractorIndex);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(facility, date, contractorIndex);
        }

        /**
         * Sets the updated facility value.
         */
        public void setFacility(String facility) {
            this.facility = facility;
        }

        /**
         * Returns the updated facility if present.
         */
        public Optional<String> getFacility() {
            return Optional.ofNullable(facility);
        }

        /**
         * Sets the updated date value.
         */
        public void setDate(LocalDate date) {
            this.date = date;
        }

        /**
         * Returns the updated date if present.
         */
        public Optional<LocalDate> getDate() {
            return Optional.ofNullable(date);
        }

        /**
         * Sets the updated contractor index (in the filtered person list).
         */
        public void setContractorIndex(Index contractorIndex) {
            this.contractorIndex = contractorIndex;
        }

        /**
         * Returns the updated contractor index if present.
         */
        public Optional<Index> getContractorIndex() {
            return Optional.ofNullable(contractorIndex);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (!(other instanceof EditTaskDescriptor)) {
                return false;
            }
            EditTaskDescriptor otherDescriptor = (EditTaskDescriptor) other;
            return Objects.equals(facility, otherDescriptor.facility)
                    && Objects.equals(date, otherDescriptor.date)
                    && Objects.equals(contractorIndex, otherDescriptor.contractorIndex);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("facility", facility)
                    .add("date", date)
                    .add("contractorIndex", contractorIndex)
                    .toString();
        }
    }
}
