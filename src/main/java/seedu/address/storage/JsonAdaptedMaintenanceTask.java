package seedu.address.storage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Name;
import seedu.address.model.person.Service;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.MaintenanceTask;

/**
 * Jackson-friendly version of {@link MaintenanceTask}.
 */
class JsonAdaptedMaintenanceTask {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "MaintenanceTask's %s field is missing!";
    public static final String INVALID_FACILITY_MESSAGE = "Facility name must not be blank and must not exceed "
            + MaintenanceTask.MAX_FACILITY_LENGTH + " characters.";

    private final String facility;
    private final String date;
    // private final int contractorIndex;
    private final String contractorName;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final String service;
    private final boolean isCompleted;

    /**
     * Constructs a {@code JsonAdaptedMaintenanceTask} with the given task details.
     */
    @JsonCreator
    public JsonAdaptedMaintenanceTask(
            @JsonProperty("facility") String facility,
            @JsonProperty("date") String date,
            @JsonProperty("contractorName") String contractorName,
            @JsonProperty("tags") List<JsonAdaptedTag> tags,
            @JsonProperty("service") String service,
            @JsonProperty("isCompleted") boolean isCompleted) {
        this.facility = facility;
        this.date = date;
        this.contractorName = contractorName;
        this.service = service;
        this.isCompleted = isCompleted;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    public JsonAdaptedMaintenanceTask(
            String facility,
            String date,
            String contractorName,
            List<JsonAdaptedTag> tags,
            String service) {
        this(facility, date, contractorName, tags, service, false);
    }

    /**
     * Converts a given {@code MaintenanceTask} into this class for Jackson use.
     *
     * @param source The task to convert. Must not be null.
     */
    public JsonAdaptedMaintenanceTask(MaintenanceTask source) {
        this.facility = source.getFacility();
        this.date = source.getDate().toString();
        this.contractorName = source.getContractorName() != null ? source.getContractorName().fullName : null;
        this.service = source.getContractorService().toString();
        this.isCompleted = source.isCompleted();
        this.tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted task into the model's
     * {@code MaintenanceTask}.
     *
     * @throws IllegalValueException if any field is missing or invalid.
     */
    public MaintenanceTask toModelType() throws IllegalValueException {
        final String modelFacility = parseFacility();
        final LocalDate modelDate = parseDate();
        final Set<Tag> modelTags = parseTags();

        if (contractorName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "contractorName"));
        }
        if (!Name.isValidName(contractorName)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelContractorName = new Name(contractorName);

        if (service == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "service"));
        }
        if (!Service.isValidService(service)) {
            throw new IllegalValueException(Service.MESSAGE_CONSTRAINTS);
        }
        final Service modelService = new Service(service);

        MaintenanceTask task = new MaintenanceTask(modelFacility, modelDate,
                modelContractorName, modelTags, modelService);
        if (isCompleted) {
            task.markAsCompleted();
        }
        return task;
    }

    /**
     * Parses and validates the facility field.
     *
     * @throws IllegalValueException if the facility is null, blank, or exceeds max
     *                               length.
     */
    private String parseFacility() throws IllegalValueException {
        if (facility == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "facility"));
        }

        String trimmedFacility = facility.trim();
        if (trimmedFacility.isEmpty() || trimmedFacility.length() > MaintenanceTask.MAX_FACILITY_LENGTH) {
            throw new IllegalValueException(INVALID_FACILITY_MESSAGE);
        }

        return trimmedFacility;
    }

    /**
     * Parses and validates the date field.
     *
     * @throws IllegalValueException if the date is null or not a valid ISO date.
     */
    private LocalDate parseDate() throws IllegalValueException {
        if (date == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "date"));
        }
        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            throw new IllegalValueException("Invalid date format: " + date);
        }
    }

    /**
     * Converts each {@code JsonAdaptedTag} in the list into a model {@code Tag}.
     *
     * @throws IllegalValueException if any tag name is invalid or if a null tag
     *                               exists.
     */
    private Set<Tag> parseTags() throws IllegalValueException {
        Set<Tag> modelTags = new HashSet<>();
        for (JsonAdaptedTag tag : tags) {
            if (tag == null) {
                throw new IllegalValueException("Tags list should not contain null elements.");
            }
            modelTags.add(tag.toModelType());
        }
        return modelTags;
    }
}
