package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Finds and lists all persons in address book whose name or service contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "findc";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names or services contain "
            + "any of the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: n/KEYWORD [MORE_KEYWORDS]... or s/KEYWORD [MORE_KEYWORDS]...\n"
            + "Examples: " + COMMAND_WORD + " n/alex david"
            + ", " + COMMAND_WORD + " s/Electrical";
    public static final String MESSAGE_NO_PERSONS_FOUND = "No matching contractors found.";
    public static final String MESSAGE_SUCCESS = "Found %d matching contractor(s).";

    private final Predicate<Person> predicate;

    /**
     * General predicate as it can be name or service.
    */
    public FindCommand(Predicate<Person> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        List<Person> filteredPersons = model.getFilteredPersonList();
        if (filteredPersons.isEmpty()) {
            return new CommandResult(MESSAGE_NO_PERSONS_FOUND);
        }

        StringBuilder personsOutput = new StringBuilder();
        for (int i = 0; i < filteredPersons.size(); i++) {
            personsOutput.append(i + 1)
                    .append(". ")
                    .append(Messages.format(filteredPersons.get(i)));
            if (i < filteredPersons.size() - 1) {
                personsOutput.append("\n");
            }
        }

        String feedback = String.format(MESSAGE_SUCCESS, filteredPersons.size()) + "\n"
                + personsOutput;
        return new CommandResult(feedback);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
