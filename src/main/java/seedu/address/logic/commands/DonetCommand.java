package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.task.MaintenanceTask;

/**
 * Marks a maintenance task as completed.
 */
public class DonetCommand extends Command {

    public static final String COMMAND_WORD = "donet";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the maintenance task identified by the "
            + "index number as completed.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Task marked as completed: %1$s";
    public static final String MESSAGE_ALREADY_COMPLETED = "This task has already been completed.";
    public static final String MESSAGE_INVALID_INDEX = "The task index provided is invalid.";

    private static final Logger logger = LogsCenter.getLogger(DonetCommand.class);

    private final Index targetIndex;

    /**
     * Creates a DonetCommand to mark the task at the given {@code Index} as completed.
     */
    public DonetCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        assert model != null : "Model should not be null";

        List<MaintenanceTask> taskList = model.getFilteredMaintenanceTaskList();

        if (targetIndex.getZeroBased() >= taskList.size()) {
            logger.warning("Invalid task index: " + targetIndex.getOneBased());
            throw new CommandException(MESSAGE_INVALID_INDEX);
        }

        MaintenanceTask taskToComplete = taskList.get(targetIndex.getZeroBased());

        if (taskToComplete.isCompleted()) {
            taskToComplete.unmarkAsCompleted();
            return new CommandResult("Task reverted to pending: " + taskToComplete.getFacility());
        }

        taskToComplete.markAsCompleted();
        logger.info("Task marked as completed: " + taskToComplete.getFacility());

        // Build display string
        List<Person> allPersons = model.getAddressBook().getPersonList();
        String contractorNameStr = taskToComplete.getContractorName() != null
            ? taskToComplete.getContractorName().fullName : "Unknown (deleted)";
        String tagsString = taskToComplete.getTags().stream()
                .map(tag -> tag.tagName)
                .collect(Collectors.joining(", "));
        String taskDisplay = taskToComplete.getFacility() + " on " + taskToComplete.getDate()
                + " (Contractor: " + contractorNameStr
                + " | Service: " + taskToComplete.getContractorService()
                + " | Tags: [" + tagsString + "])";
        return new CommandResult(String.format(MESSAGE_SUCCESS, taskDisplay));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DonetCommand)) {
            return false;
        }
        DonetCommand otherCommand = (DonetCommand) other;
        return targetIndex.equals(otherCommand.targetIndex);
    }
}
