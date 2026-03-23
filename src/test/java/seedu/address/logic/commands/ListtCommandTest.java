package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

import java.time.LocalDate;

/**
 * Contains unit tests for ListtCommand.
 */
public class ListtCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_emptyTaskList_showsNoTasksMessage() {
        CommandResult result = new ListtCommand().execute(model);
        assertEquals(ListtCommand.MESSAGE_NO_TASKS, result.getFeedbackToUser());
    }

    @Test
    public void execute_oneTask_showsTask() throws Exception {
        // Add a task first
        new AddtCommand("Sports Hall",
                LocalDate.of(2026, 12, 1),
                seedu.address.commons.core.index.Index.fromOneBased(1)).execute(model);

        CommandResult result = new ListtCommand().execute(model);
        String feedback = result.getFeedbackToUser();

        // Should contain success header
        assert feedback.contains(ListtCommand.MESSAGE_SUCCESS);
        // Should contain facility name
        assert feedback.contains("Sports Hall");
    }

    @Test
    public void execute_multipleTasks_showsAllTasks() throws Exception {
        new AddtCommand("Sports Hall",
                LocalDate.of(2026, 12, 1),
                seedu.address.commons.core.index.Index.fromOneBased(1)).execute(model);
        new AddtCommand("Swimming Pool",
                LocalDate.of(2026, 12, 5),
                seedu.address.commons.core.index.Index.fromOneBased(2)).execute(model);

        CommandResult result = new ListtCommand().execute(model);
        String feedback = result.getFeedbackToUser();

        assert feedback.contains("Sports Hall");
        assert feedback.contains("Swimming Pool");
    }
}
