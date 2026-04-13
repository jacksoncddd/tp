package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Clears the address book but preserves completed maintenance tasks for
 * reporting.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String CONFIRMATION_KEYWORD = "confirm";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared! "
            + "Completed tasks were preserved for reporting.";
    public static final String MESSAGE_CONFIRMATION_REQUIRED = "WARNING: "
            + "This will delete ALL contacts and pending tasks permanently!\n"
            + "Completed tasks are preserved for monthly reporting.\n"
            + "To confirm, please type: clear confirm";

    private final boolean isConfirmed;

    public ClearCommand(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (!isConfirmed) {
            return new CommandResult(MESSAGE_CONFIRMATION_REQUIRED);
        }

        model.setAddressBook(new AddressBook());
        model.getMaintenanceTaskList().clearUncompletedTasks();

        return new CommandResult(MESSAGE_SUCCESS);
    }
}
