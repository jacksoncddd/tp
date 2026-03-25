package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Sorts all maintenance tasks by date.
 */
public class SortTaskCommand extends Command {

    public static final String COMMAND_WORD = "sortt";

    public static final String MESSAGE_SUCCESS = "Tasks sorted by date.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        model.sortTasksByDate();

        return new CommandResult(MESSAGE_SUCCESS);
    }
}
