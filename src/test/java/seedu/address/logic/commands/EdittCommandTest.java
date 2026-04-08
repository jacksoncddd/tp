package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EdittCommand.EditTaskDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.task.MaintenanceTask;

/**
 * Contains unit tests for EdittCommand.
 */
public class EdittCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() throws Exception {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        new AddtCommand("Sports Hall", LocalDate.now().plusDays(10), Index.fromOneBased(1)).execute(model);
    }

    @Test
    public void execute_editFacility_success() throws Exception {
        EditTaskDescriptor descriptor = new EditTaskDescriptor();
        descriptor.setFacility("Swimming Pool");
        EdittCommand command = new EdittCommand(Index.fromOneBased(1), descriptor);

        command.execute(model);

        assertEquals("Swimming Pool", model.getMaintenanceTaskList().getTasks().get(0).getFacility());
    }

    @Test
    public void execute_editDateAndContractor_success() throws Exception {
        EditTaskDescriptor descriptor = new EditTaskDescriptor();
        LocalDate newDate = LocalDate.now().plusDays(20);
        descriptor.setDate(newDate);
        descriptor.setContractorIndex(Index.fromOneBased(2));
        EdittCommand command = new EdittCommand(Index.fromOneBased(1), descriptor);

        command.execute(model);

        assertEquals(newDate, model.getMaintenanceTaskList().getTasks().get(0).getDate());

        Name expectedName = model.getFilteredPersonList().get(1).getName();
        assertEquals(expectedName, model.getMaintenanceTaskList().getTasks().get(0).getContractorName());
    }

    @Test
    public void execute_invalidTaskIndex_throwsCommandException() {
        EditTaskDescriptor descriptor = new EditTaskDescriptor();
        descriptor.setFacility("Swimming Pool");
        EdittCommand command = new EdittCommand(Index.fromOneBased(999), descriptor);

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_invalidContractorIndex_throwsCommandException() {
        EditTaskDescriptor descriptor = new EditTaskDescriptor();
        descriptor.setContractorIndex(Index.fromOneBased(999));
        EdittCommand command = new EdittCommand(Index.fromOneBased(1), descriptor);

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_duplicateTask_throwsCommandException() throws Exception {
        new AddtCommand("Swimming Pool", LocalDate.now().plusDays(12), Index.fromOneBased(2)).execute(model);

        EditTaskDescriptor descriptor = new EditTaskDescriptor();
        descriptor.setFacility("Swimming Pool");
        descriptor.setDate(LocalDate.now().plusDays(12));
        EdittCommand command = new EdittCommand(Index.fromOneBased(1), descriptor);

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_completedTask_throwsCommandException() throws Exception {
        new DonetCommand(Index.fromOneBased(1)).execute(model);

        EditTaskDescriptor descriptor = new EditTaskDescriptor();
        descriptor.setFacility("Swimming Pool");
        EdittCommand command = new EdittCommand(Index.fromOneBased(1), descriptor);

        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(EdittCommand.MESSAGE_CANNOT_EDIT_COMPLETED_TASK, exception.getMessage());
    }

    @Test
    public void execute_facilityOnlyEdit_preservesDateAndContractor() throws Exception {
        List<MaintenanceTask> tasks = model.getMaintenanceTaskList().getTasks();
        MaintenanceTask originalTask = tasks.get(0);

        EditTaskDescriptor descriptor = new EditTaskDescriptor();
        descriptor.setFacility("Main Lobby");
        EdittCommand command = new EdittCommand(Index.fromOneBased(1), descriptor);

        command.execute(model);

        MaintenanceTask editedTask = tasks.get(0);
        assertEquals("Main Lobby", editedTask.getFacility());
        assertEquals(originalTask.getDate(), editedTask.getDate());
        assertEquals(originalTask.getContractorName(), editedTask.getContractorName());
    }

    @Test
    public void execute_editContractorWithFilteredList_success() throws Exception {
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(List.of("George")));
        assertEquals(1, model.getFilteredPersonList().size());

        EditTaskDescriptor descriptor = new EditTaskDescriptor();
        descriptor.setContractorIndex(Index.fromOneBased(1));
        EdittCommand command = new EdittCommand(Index.fromOneBased(1), descriptor);

        command.execute(model);

        // Assert that the contractor assigned is indeed George
        Name expectedName = model.getFilteredPersonList().get(0).getName();
        assertEquals(expectedName, model.getMaintenanceTaskList().getTasks().get(0).getContractorName());
    }
}
