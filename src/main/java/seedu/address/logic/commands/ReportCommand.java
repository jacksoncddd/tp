package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MONTH;

import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import seedu.address.model.Model;
import seedu.address.model.person.Name;
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
     * Constructs a {@code ReportCommand} that generates a monthly maintenance
     * report
     * for the specified year and month.
     *
     * @param yearMonth the {@link YearMonth} representing the month for which the
     *                  maintenance report should be generated; must not be
     *                  {@code null}
     * @throws NullPointerException if {@code yearMonth} is {@code null}
     */

    public ReportCommand(YearMonth yearMonth) {
        requireNonNull(yearMonth);
        this.yearMonth = yearMonth;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        List<MaintenanceTask> allTasks = model.getMaintenanceTaskList().getTasks();

        List<MaintenanceTask> filteredTasks = allTasks.stream()
                .filter(task -> YearMonth.from(task.getDate()).equals(yearMonth))
                .filter(MaintenanceTask::isCompleted)
                .collect(Collectors.toList());

        if (filteredTasks.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_NO_TASKS, yearMonth));
        }

        // Group tasks by contractor Name instead of Index
        Map<Name, List<MaintenanceTask>> tasksByContractor = new LinkedHashMap<>();
        for (MaintenanceTask task : filteredTasks) {
            tasksByContractor
                    .computeIfAbsent(task.getContractorName(), k -> new java.util.ArrayList<>())
                    .add(task);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(MESSAGE_SUCCESS, yearMonth, filteredTasks.size()));

        int contractorNumber = 1;
        for (Map.Entry<Name, List<MaintenanceTask>> entry : tasksByContractor.entrySet()) {
            Name contractorName = entry.getKey();
            List<MaintenanceTask> tasks = entry.getValue();

            // Since tasks are grouped by contractor, we can fetch service and tags from the
            // first task
            MaintenanceTask representativeTask = tasks.get(0);
            String service = representativeTask.getContractorService() != null
                    ? representativeTask.getContractorService().toString()
                    : "Unknown";
            String tagsString = representativeTask.getTags().stream()
                    .map(tag -> tag.tagName)
                    .collect(Collectors.joining(", "));

            sb.append(contractorNumber).append(". ")
                    .append(contractorName.fullName)
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
