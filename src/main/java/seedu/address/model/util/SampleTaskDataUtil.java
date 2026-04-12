package seedu.address.model.util;

import java.time.LocalDate;

import seedu.address.model.person.Person;
import seedu.address.model.task.MaintenanceTask;
import seedu.address.model.task.MaintenanceTaskList;

/**
 * Contains utility methods for populating {@code MaintenanceTaskList} with sample data.
 */
public class SampleTaskDataUtil {

    /**
     * Returns sample maintenance tasks tied to sample contractors.
     */
    public static MaintenanceTask[] getSampleTasks() {
        Person[] sampleContractors = SampleContractorDataUtil.getSamplePersons();

        return new MaintenanceTask[] {
            new MaintenanceTask("North Tower Lift Lobby", LocalDate.of(2026, 5, 4),
                sampleContractors[2].getName(), sampleContractors[2].getTags(), sampleContractors[2].getService()),
            new MaintenanceTask("Block C Main Switch Room", LocalDate.of(2026, 5, 8),
                sampleContractors[1].getName(), sampleContractors[1].getTags(), sampleContractors[1].getService()),
            new MaintenanceTask("Community Pool Pump Room", LocalDate.of(2026, 5, 11),
                sampleContractors[4].getName(), sampleContractors[4].getTags(), sampleContractors[4].getService()),
            new MaintenanceTask("Tower B Chiller Plant", LocalDate.of(2026, 5, 14),
                sampleContractors[3].getName(), sampleContractors[3].getTags(), sampleContractors[3].getService()),
            new MaintenanceTask("Block A Water Riser", LocalDate.of(2026, 5, 18),
                sampleContractors[0].getName(), sampleContractors[0].getTags(), sampleContractors[0].getService())
        };
    }

    /**
     * Returns a {@code MaintenanceTaskList} populated with sample tasks.
     */
    public static MaintenanceTaskList getSampleTaskList() {
        MaintenanceTaskList sampleTaskList = new MaintenanceTaskList();
        for (MaintenanceTask sampleTask : getSampleTasks()) {
            sampleTaskList.addTask(sampleTask);
        }
        return sampleTaskList;
    }
}
