package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedMaintenanceTask.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalMaintenanceTasks.SPORTS_HALL;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.MaintenanceTask;

public class JsonAdaptedMaintenanceTaskTest {

    private static final String VALID_FACILITY = SPORTS_HALL.getFacility();
    private static final String VALID_DATE = SPORTS_HALL.getDate().toString();
    private static final String VALID_CONTRACTOR_NAME = SPORTS_HALL.getContractorName().fullName;
    private static final String VALID_SERVICE = SPORTS_HALL.getContractorService().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = SPORTS_HALL.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validTaskDetails_returnsTask() throws Exception {
        JsonAdaptedMaintenanceTask task = new JsonAdaptedMaintenanceTask(SPORTS_HALL);
        assertEquals(SPORTS_HALL, task.toModelType());
    }

    @Test
    public void toModelType_nullFacility_throwsIllegalValueException() {
        JsonAdaptedMaintenanceTask task = new JsonAdaptedMaintenanceTask(
                null, VALID_DATE, VALID_CONTRACTOR_NAME, VALID_TAGS, VALID_SERVICE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "facility");
        assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        JsonAdaptedMaintenanceTask task = new JsonAdaptedMaintenanceTask(
                VALID_FACILITY, null, VALID_CONTRACTOR_NAME, VALID_TAGS, VALID_SERVICE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "date");
        assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        JsonAdaptedMaintenanceTask task = new JsonAdaptedMaintenanceTask(
                VALID_FACILITY, "not-a-date", VALID_CONTRACTOR_NAME, VALID_TAGS, VALID_SERVICE);
        assertThrows(IllegalValueException.class, task::toModelType);
    }

    @Test
    public void toModelType_invalidTag_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = List.of(new JsonAdaptedTag("#invalid"));
        JsonAdaptedMaintenanceTask task = new JsonAdaptedMaintenanceTask(
                VALID_FACILITY, VALID_DATE, VALID_CONTRACTOR_NAME, invalidTags, VALID_SERVICE);
        assertThrows(IllegalValueException.class, task::toModelType);
    }

    /**
     * Verifies that constructing from a model {@code MaintenanceTask} and
     * converting back
     * produces an equal task (round-trip integrity).
     */
    private void assertRoundTrip(MaintenanceTask original) throws Exception {
        JsonAdaptedMaintenanceTask adapted = new JsonAdaptedMaintenanceTask(original);
        assertEquals(original, adapted.toModelType());
    }

    @Test
    public void toModelType_roundTrip_success() throws Exception {
        assertRoundTrip(SPORTS_HALL);
    }
}
