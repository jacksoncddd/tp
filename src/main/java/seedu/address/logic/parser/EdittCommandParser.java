package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTRACTOR_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FACILITY;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EdittCommand;
import seedu.address.logic.commands.EdittCommand.EditTaskDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.task.MaintenanceTask;

/**
 * Parses input arguments and creates a new {@code EdittCommand} object.
 */
public class EdittCommandParser implements Parser<EdittCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of {@code EdittCommand}
     * and returns an {@code EdittCommand} object for execution.
     *
     * @param args raw user arguments.
     * @return parsed {@code EdittCommand}.
     * @throws ParseException if the user input does not adhere to the expected format.
     */
    @Override
    public EdittCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_FACILITY, PREFIX_DATE, PREFIX_CONTRACTOR_INDEX);

        Index targetIndex;
        try {
            targetIndex = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EdittCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_FACILITY, PREFIX_DATE, PREFIX_CONTRACTOR_INDEX);

        EditTaskDescriptor descriptor = new EditTaskDescriptor();

        if (argMultimap.getValue(PREFIX_FACILITY).isPresent()) {
            String facility = argMultimap.getValue(PREFIX_FACILITY).get().trim();
            if (facility.isEmpty() || facility.length() > MaintenanceTask.MAX_FACILITY_LENGTH) {
                throw new ParseException("Facility must be between 1 and 50 characters.");
            }
            descriptor.setFacility(facility);
        }

        if (argMultimap.getValue(PREFIX_DATE).isPresent()) {
            String dateStr = argMultimap.getValue(PREFIX_DATE).get().trim();
            try {
                LocalDate date = LocalDate.parse(dateStr);
                if (date.isBefore(LocalDate.now())) {
                    throw new ParseException("Date must not be in the past.");
                }
                descriptor.setDate(date);
            } catch (DateTimeParseException e) {
                throw new ParseException("Date must be in YYYY-MM-DD format and be a valid date.");
            }
        }

        if (argMultimap.getValue(PREFIX_CONTRACTOR_INDEX).isPresent()) {
            try {
                descriptor.setContractorIndex(ParserUtil.parseIndex(
                        argMultimap.getValue(PREFIX_CONTRACTOR_INDEX).get()));
            } catch (ParseException e) {
                throw new ParseException("Contractor index must be a positive integer.");
            }
        }

        if (!descriptor.isAnyFieldEdited()) {
            throw new ParseException(EdittCommand.MESSAGE_NOT_EDITED);
        }

        return new EdittCommand(targetIndex, descriptor);
    }
}
