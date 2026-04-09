package seedu.address.logic.commands;

import java.util.List;

import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.task.MaintenanceTask;
import seedu.address.model.task.MaintenanceTaskList;

/**
 * Lists all maintenance tasks in the task list to the user.
 */
public class ListtCommand extends Command {

    public static final String COMMAND_WORD = "listt";
    public static final String MESSAGE_NO_TASKS = "No maintenance tasks found.";
    public static final String MESSAGE_SUCCESS = "Listed all maintenance tasks:";

    @Override
    public CommandResult execute(Model model) {
        assert model != null : "Model should not be null";
        MaintenanceTaskList taskList = model.getMaintenanceTaskList();
        List<MaintenanceTask> tasks = taskList.getTasks();
        List<Person> allPersons = model.getAddressBook().getPersonList();

        if (tasks.isEmpty()) {
            return new CommandResult(MESSAGE_NO_TASKS);
        }

        StringBuilder sb = new StringBuilder(MESSAGE_SUCCESS + "\n");

        for (int i = 0; i < tasks.size(); i++) {
            MaintenanceTask task = tasks.get(i);
            String contractorName = task.getContractorName() != null ? task.getContractorName().fullName : "Unknown";
            String service = task.getContractorService() != null ? task.getContractorService().toString() : "Unknown";
            String tagsString = task.getTags().stream()
                    .map(tag -> tag.tagName)
                    .collect(java.util.stream.Collectors.joining(", "));

            sb.append(i + 1).append(". ")
                    .append(task.isCompleted() ? "[DONE] " : "[PENDING] ")
                    .append(task.getFacility())
                    .append(" on ").append(task.getDate())
                    .append(" (Contractor: ").append(contractorName)
                    .append(" | Service: ").append(service)
                    .append(" | Tags: [").append(tagsString).append("])\n");
        }
        return new CommandResult(sb.toString());
    }
}
