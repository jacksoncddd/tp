package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "listc";

    public static final String MESSAGE_SUCCESS = "Listed all contractors:";
    public static final String MESSAGE_NO_CONTRACTORS = "No contractors found.";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        List<Person> allPersons = model.getFilteredPersonList();

        if (allPersons.isEmpty()) {
            return new CommandResult(MESSAGE_NO_CONTRACTORS);
        }

        StringBuilder sb = new StringBuilder(MESSAGE_SUCCESS + "\n");
        for (int i = 0; i < allPersons.size(); i++) {
            sb.append(i + 1).append(". ").append(Messages.format(allPersons.get(i)));
            if (i < allPersons.size() - 1) {
                sb.append("\n");
            }
        }
        return new CommandResult(sb.toString());
    }
}
