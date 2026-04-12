package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTRACTOR_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FACILITY;

import java.time.LocalDate;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Service;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.MaintenanceTask;

/**
 * Adds a maintenance task to the task list.
 *
 * <p>
 * Command format: {@code addt f/FACILITY d/DATE c/CONTRACTOR_INDEX}
 */
public class AddtCommand extends Command {

    public static final String COMMAND_WORD = "addt";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a maintenance task. "
            + "Parameters: "
            + PREFIX_FACILITY + "FACILITY "
            + PREFIX_DATE + "DATE (YYYY-MM-DD) "
            + PREFIX_CONTRACTOR_INDEX + "CONTRACTOR_INDEX\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_FACILITY + "Sports Hall "
            + PREFIX_DATE + "2026-12-01 "
            + PREFIX_CONTRACTOR_INDEX + "2";

    public static final String MESSAGE_SUCCESS = "Task added successfully: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "A task for this facility on the same date already exists.";
    public static final String MESSAGE_INVALID_CONTRACTOR_INDEX = "The contractor index provided is invalid.";

    private final String facility;
    private final LocalDate date;
    private final Index contractorIndex;

    /**
     * Creates an {@code AddtCommand} to add a task with the given parameters.
     *
     * @param facility        The facility name (1–50 chars).
     * @param date            The scheduled date (not in the past).
     * @param contractorIndex The 1-based index of the contractor to assign.
     */
    public AddtCommand(String facility, LocalDate date, Index contractorIndex) {
        requireNonNull(facility);
        requireNonNull(date);
        requireNonNull(contractorIndex);
        this.facility = facility;
        this.date = date;
        this.contractorIndex = contractorIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        validateContractorExists(model);
        validateNoDuplicate(model);

        Person contractor = model.getAddressBook().getPersonList().get(contractorIndex.getZeroBased());
        Set<Tag> contractorTags = contractor.getTags();
        Service contractorService = contractor.getService();

        MaintenanceTask task = new MaintenanceTask(facility, date, contractor.getName(),
                contractorTags, contractorService);
        model.getMaintenanceTaskList().addTask(task);

        String tagsString = contractorTags.stream()
                .map(tag -> tag.tagName)
                .collect(java.util.stream.Collectors.joining(", "));
        String taskDisplay = facility + " on " + date
                + " (Contractor: " + contractor.getName().fullName
                + " | Service: " + contractorService
                + " | Tags: [" + tagsString + "])";
        return new CommandResult(String.format(MESSAGE_SUCCESS, taskDisplay));
    }

    /**
     * Validates that the contractor index refers to an existing contractor in the
     * model.
     *
     * @param model The current model.
     * @throws CommandException If the index is out of bounds.
     */
    private void validateContractorExists(Model model) throws CommandException {
        int listSize = model.getAddressBook().getPersonList().size();
        if (contractorIndex.getZeroBased() >= listSize) {
            throw new CommandException(MESSAGE_INVALID_CONTRACTOR_INDEX);
        }
    }

    /**
     * Validates that no existing task has the same facility and date.
     *
     * @param model The current model.
     * @throws CommandException If a duplicate task is found.
     */
    private void validateNoDuplicate(Model model) throws CommandException {
        if (model.getMaintenanceTaskList().hasDuplicate(facility, date)) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }
    }
}
