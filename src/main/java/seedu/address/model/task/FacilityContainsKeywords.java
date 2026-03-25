package seedu.address.model.task;

import java.util.function.Predicate;

/**
 * Tests that a {@code MaintenanceTask}'s {@code Facility} matches the given
 * string.
 */
public class FacilityContainsKeywords implements Predicate<MaintenanceTask> {
    private final String keyword;

    public FacilityContainsKeywords(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean test(MaintenanceTask task) {
        return task.getFacility().equalsIgnoreCase(keyword);
    }

    @Override
    public boolean equals(Object other) {
        return other == this || (other instanceof FacilityContainsKeywords
                && keyword.equals(((FacilityContainsKeywords) other).keyword));
    }
}
