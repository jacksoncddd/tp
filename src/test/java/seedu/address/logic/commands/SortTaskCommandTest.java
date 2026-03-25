package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.MaintenanceTaskList;

public class SortTaskCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_tasksSortedByAscendingDate() throws CommandException {
        // Add out-of-order: later date first, earlier date second.
        new AddtCommand("Swimming Pool",
                LocalDate.of(2026, 12, 15), Index.fromOneBased(2)).execute(model);
        new AddtCommand("Sports Hall",
                LocalDate.of(2026, 12, 1), Index.fromOneBased(1)).execute(model);

        new SortTaskCommand().execute(model);

        MaintenanceTaskList taskList = model.getMaintenanceTaskList();
        assertEquals(2, taskList.size());
        assertEquals("Sports Hall", taskList.getTasks().get(0).getFacility());
        assertEquals("Swimming Pool", taskList.getTasks().get(1).getFacility());
    }

    @Test
    public void execute_emptyList_success() {
        new SortTaskCommand().execute(model);
        assertEquals(0, model.getMaintenanceTaskList().size());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SortTaskCommand().execute(null));
    }
}

