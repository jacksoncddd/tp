package seedu.address.logic.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

/**
 * Contains unit tests for DeltCommand.
 */
public class DeltCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndex_success() throws Exception {
        // Add a task first
        new AddtCommand("Sports Hall",
                LocalDate.of(2026, 12, 1), Index.fromOneBased(1)).execute(model);

        // Delete it
        new DeltCommand(Index.fromOneBased(1)).execute(model);

        // Task list should be empty
        assertTrue(model.getMaintenanceTaskList().isEmpty());
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        DeltCommand command = new DeltCommand(Index.fromOneBased(999));
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_deleteFirstOfMultiple_success() throws Exception {
        new AddtCommand("Sports Hall",
                LocalDate.of(2026, 12, 1), Index.fromOneBased(1)).execute(model);
        new AddtCommand("Swimming Pool",
                LocalDate.of(2026, 12, 5), Index.fromOneBased(2)).execute(model);

        new DeltCommand(Index.fromOneBased(1)).execute(model);

        // Only one task should remain
        assertEquals(1, model.getMaintenanceTaskList().size());
        // Remaining task should be Swimming Pool
        assertEquals("Swimming Pool", model.getMaintenanceTaskList()
                .getTasks().get(0).getFacility());
    }

    @Test
    public void execute_deleteAllTasks_emptyList() throws Exception {
        new AddtCommand("Sports Hall",
                LocalDate.of(2026, 12, 1), Index.fromOneBased(1)).execute(model);
        new AddtCommand("Swimming Pool",
                LocalDate.of(2026, 12, 5), Index.fromOneBased(2)).execute(model);

        new DeltCommand(Index.fromOneBased(1)).execute(model);
        new DeltCommand(Index.fromOneBased(1)).execute(model);

        assertTrue(model.getMaintenanceTaskList().isEmpty());
    }
}
