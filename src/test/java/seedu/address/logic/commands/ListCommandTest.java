package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        List<Person> persons = expectedModel.getFilteredPersonList();
        StringBuilder sb = new StringBuilder(ListCommand.MESSAGE_SUCCESS + "\n");
        for (int i = 0; i < persons.size(); i++) {
            sb.append(i + 1).append(". ").append(Messages.format(persons.get(i)));
            if (i < persons.size() - 1) {
                sb.append("\n");
            }
        }
        assertCommandSuccess(new ListCommand(), model, sb.toString(), expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        List<Person> persons = expectedModel.getFilteredPersonList();
        StringBuilder sb = new StringBuilder(ListCommand.MESSAGE_SUCCESS + "\n");
        for (int i = 0; i < persons.size(); i++) {
            sb.append(i + 1).append(". ").append(Messages.format(persons.get(i)));
            if (i < persons.size() - 1) {
                sb.append("\n");
            }
        }
        assertCommandSuccess(new ListCommand(), model, sb.toString(), expectedModel);
    }
}
