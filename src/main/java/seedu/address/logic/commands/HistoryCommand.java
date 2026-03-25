public class HistoryCommand extends Command {
    public static final String COMMAND_WORD = "history";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all tasks for a facility. "
            + "Parameters: f/FACILITY";
    public static final String MESSAGE_SUCCESS = "Listed all maintenance history for: %1$s";

    private final FacilityContainsKeywords predicate;
    private final String facilityName;

    public HistoryCommand(String facilityName) {
        this.facilityName = facilityName;
        this.predicate = new FacilityContainsKeywords(facilityName);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredMaintenanceTaskList(predicate);
        return new CommandResult(String.format(MESSAGE_SUCCESS, facilityName));
    }
}
