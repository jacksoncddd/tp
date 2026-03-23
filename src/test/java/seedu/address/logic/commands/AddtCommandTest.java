package seedu.address.logic.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

/**
 * Contains unit tests for AddtCommand.
 */
public class AddtCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validInput_success() throws Exception {
        AddtCommand command = new AddtCommand("Sports Hall",
                LocalDate.of(2026, 12, 1), Index.fromOneBased(1));
        command.execute(model);
        assertEquals(1, model.getMaintenanceTaskList().size());
    }

    @Test
    public void execute_invalidContractorIndex_throwsCommandException() {
        AddtCommand command = new AddtCommand("Sports Hall",
                LocalDate.of(2026, 12, 1), Index.fromOneBased(999));
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_duplicateTask_throwsCommandException() throws Exception {
        AddtCommand command = new AddtCommand("Sports Hall",
                LocalDate.of(2026, 12, 1), Index.fromOneBased(1));
        command.execute(model);
        AddtCommand duplicateCommand = new AddtCommand("Sports Hall",
                LocalDate.of(2026, 12, 1), Index.fromOneBased(1));
        assertThrows(CommandException.class, () -> duplicateCommand.execute(model));
    }

    @Test
    public void execute_multipleTasks_success() throws Exception {
        new AddtCommand("Sports Hall",
                LocalDate.of(2026, 12, 1), Index.fromOneBased(1)).execute(model);
        new AddtCommand("Swimming Pool",
                LocalDate.of(2026, 12, 5), Index.fromOneBased(2)).execute(model);
        assertEquals(2, model.getMaintenanceTaskList().size());
    }

    @Test
    public void execute_taskAddedHasCorrectContractor_success() throws Exception {
        AddtCommand command = new AddtCommand("Sports Hall",
                LocalDate.of(2026, 12, 1), Index.fromOneBased(1));
        command.execute(model);
        assertEquals(1, model.getMaintenanceTaskList()
                .getTasks().get(0).getContractorIndex());
    }
}
