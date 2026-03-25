package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

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
