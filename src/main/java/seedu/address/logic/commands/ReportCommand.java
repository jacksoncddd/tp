package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MONTH;

import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.task.MaintenanceTask;

/**
 * Generates a monthly report of maintenance tasks.
 */
public class ReportCommand extends Command {

    public static final String COMMAND_WORD = "report";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Generates a monthly maintenance report. "
            + "Parameters: "
            + PREFIX_MONTH + "YEAR-MONTH (e.g. 2026-12)\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_MONTH + "2026-12";

    public static final String MESSAGE_NO_TASKS = "No completed maintenance tasks found for %1$s.";
    public static final String MESSAGE_SUCCESS = "Report for %1$s:\nTotal tasks: %2$d\n\nTasks by contractor:\n";

    private final YearMonth yearMonth;

    /**
     * Creates a ReportCommand to generate a report for the given {@code YearMonth}.
     */
    public ReportCommand(YearMonth yearMonth) {
        requireNonNull(yearMonth);
        this.yearMonth = yearMonth;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        List<MaintenanceTask> allTasks = model.getMaintenanceTaskList().getTasks();

        // Filter tasks by the specified month
        List<MaintenanceTask> filteredTasks = allTasks.stream()
                .filter(task -> YearMonth.from(task.getDate()).equals(yearMonth))
                .filter(task -> task.isCompleted())
                .collect(Collectors.toList());

        if (filteredTasks.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_NO_TASKS, yearMonth));
        }

        // Group tasks by contractor index
        Map<Integer, List<MaintenanceTask>> tasksByContractor = new LinkedHashMap<>();
        for (MaintenanceTask task : filteredTasks) {
            tasksByContractor
                    .computeIfAbsent(task.getContractorIndex(), k -> new java.util.ArrayList<>())
                    .add(task);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(MESSAGE_SUCCESS, yearMonth, filteredTasks.size()));

        List<Person> allPersons = model.getAddressBook().getPersonList();

        int contractorNumber = 1;
        for (Map.Entry<Integer, List<MaintenanceTask>> entry : tasksByContractor.entrySet()) {
            int contractorIndex = entry.getKey();
            List<MaintenanceTask> tasks = entry.getValue();

            // Look up contractor details
            int idx = contractorIndex - 1;
            if (idx < 0 || idx >= allPersons.size()) {
                sb.append(contractorNumber).append(". [Contractor #").append(contractorIndex)
                        .append(" no longer exists] | Tasks: ").append(tasks.size()).append("\n");
                contractorNumber++;
                continue;
            }

            int contractorIdx = contractorIndex - 1;

            String contractorName;
            String service;
            String tagsString;

            if (contractorIdx < allPersons.size()) {
                Person contractor = allPersons.get(contractorIdx);
                contractorName = contractor.getName().fullName;
                service = contractor.getService().toString();
                tagsString = contractor.getTags().stream()
                        .map(tag -> tag.tagName)
                        .collect(Collectors.joining(", "));
            } else {
                contractorName = "Unknown (deleted)";
                service = "Unknown";
                tagsString = "";
            }

            sb.append(contractorNumber).append(". ")
                    .append(contractorName)
                    .append(" | Service: ").append(service)
                    .append(" | Tags: [").append(tagsString).append("]")
                    .append(" | Tasks: ").append(tasks.size()).append("\n");

            for (MaintenanceTask task : tasks) {
                sb.append("   - ").append(task.getFacility())
                        .append(" on ").append(task.getDate()).append("\n");
            }

            contractorNumber++;
        }

        return new CommandResult(sb.toString());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ReportCommand)) {
            return false;
        }
        ReportCommand otherCommand = (ReportCommand) other;
        return yearMonth.equals(otherCommand.yearMonth);
    }
}
