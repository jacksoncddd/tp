package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalMaintenanceTasks.getTypicalTaskList;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.FacilityContainsKeywords;
import seedu.address.model.task.MaintenanceTask;

/**
 * Contains integration tests (interaction with the Model) for
 * {@code HistoryCommand}.
 */
public class HistoryCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @BeforeEach
    public void setUp() {
        // Load typical tasks into both models so that searches actually yield results
        for (MaintenanceTask task : getTypicalTaskList().getTasks()) {
            model.getMaintenanceTaskList().addTask(task);
            expectedModel.getMaintenanceTaskList().addTask(task);
        }
    }

    @Test
    public void equals() {
        HistoryCommand historyGymCommand = new HistoryCommand("Gym");
        HistoryCommand historyPoolCommand = new HistoryCommand("Pool");

        // same object -> returns true
        assertTrue(historyGymCommand.equals(historyGymCommand));

        // same values -> returns true
        HistoryCommand historyGymCommandCopy = new HistoryCommand("Gym");
        assertTrue(historyGymCommand.equals(historyGymCommandCopy));

        // different types -> returns false
        assertFalse(historyGymCommand.equals(1));

        // null -> returns false
        assertFalse(historyGymCommand.equals(null));

        // different facility -> returns false
        assertFalse(historyGymCommand.equals(historyPoolCommand));
    }

    @Test
    public void execute_unknownFacility_noTasksFound() {
        String facilityName = "NonExistentFacility";
        String expectedMessage = String.format(HistoryCommand.MESSAGE_NO_TASKS, facilityName);

        HistoryCommand command = new HistoryCommand(facilityName);
        FacilityContainsKeywords predicate = new FacilityContainsKeywords(facilityName);
        expectedModel.updateFilteredMaintenanceTaskList(predicate);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(java.util.Collections.emptyList(), model.getFilteredMaintenanceTaskList());
    }

    @Test
    public void execute_knownFacility_tasksFound() {
        String facilityName = "Sports Hall";

        HistoryCommand command = new HistoryCommand(facilityName);
        FacilityContainsKeywords predicate = new FacilityContainsKeywords(facilityName);
        expectedModel.updateFilteredMaintenanceTaskList(predicate);

        List<MaintenanceTask> matchingTasks = expectedModel.getFilteredMaintenanceTaskList();
        String taskListString = IntStream.range(0, matchingTasks.size())
                .mapToObj(i -> (i + 1) + ". " + matchingTasks.get(i).toString())
                .collect(Collectors.joining("\n"));
        String expectedMessage = String.format(HistoryCommand.MESSAGE_SUCCESS, facilityName, taskListString);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);

        assertTrue(model.getFilteredMaintenanceTaskList().size() > 0);

        model.getFilteredMaintenanceTaskList().forEach(task -> assertTrue(task.getFacility().equals(facilityName),
                "Expected facility: " + facilityName + " but got: " + task.getFacility()));
    }

    @Test
    public void execute_facilityNameCaseInsensitive_tasksFound() {
        String facilityName = "sports hall";

        HistoryCommand command = new HistoryCommand(facilityName);
        FacilityContainsKeywords predicate = new FacilityContainsKeywords(facilityName);
        expectedModel.updateFilteredMaintenanceTaskList(predicate);

        List<MaintenanceTask> matchingTasks = expectedModel.getFilteredMaintenanceTaskList();
        String taskListString = IntStream.range(0, matchingTasks.size())
                .mapToObj(i -> (i + 1) + ". " + matchingTasks.get(i).toString())
                .collect(Collectors.joining("\n"));
        String expectedMessage = String.format(HistoryCommand.MESSAGE_SUCCESS, facilityName, taskListString);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);

        assertTrue(model.getFilteredMaintenanceTaskList().size() > 0);
        model.getFilteredMaintenanceTaskList().forEach(task ->
                assertTrue(task.getFacility().equalsIgnoreCase(facilityName)));
    }

    @Test
    public void toStringMethod() {
        HistoryCommand historyCommand = new HistoryCommand("Gym");
        String expected = HistoryCommand.class.getCanonicalName() + "{facilityName=Gym}";
        assertEquals(expected, historyCommand.toString());
    }
}
