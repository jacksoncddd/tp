package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTRACTOR_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FACILITY;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddtCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.task.MaintenanceTask;

/**
 * Parses input arguments and creates a new {@code AddtCommand} object.
 */
public class AddtCommandParser implements Parser<AddtCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code AddtCommand}
     * and returns an {@code AddtCommand} object for execution.
     *
     * @param args The raw argument string provided by the user.
     * @return A ready-to-execute {@code AddtCommand}.
     * @throws ParseException If the user input does not conform to the expected format.
     */
    @Override
    public AddtCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = tokenizeArguments(args);
        validateAllPrefixesPresent(argMultimap);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_FACILITY, PREFIX_DATE, PREFIX_CONTRACTOR_INDEX);

        String facility = parseFacility(argMultimap);
        LocalDate date = parseDate(argMultimap);
        Index contractorIndex = parseContractorIndex(argMultimap);

        return new AddtCommand(facility, date, contractorIndex);
    }

    /**
     * Tokenizes the raw argument string using the expected prefixes.
     *
     * @param args The raw argument string.
     * @return A populated {@code ArgumentMultimap}.
     */
    private ArgumentMultimap tokenizeArguments(String args) {
        return ArgumentTokenizer.tokenize(args, PREFIX_FACILITY, PREFIX_DATE, PREFIX_CONTRACTOR_INDEX);
    }

    /**
     * Validates that all required prefixes are present and that there is no preamble.
     *
     * @param argMultimap The tokenized argument map.
     * @throws ParseException If any required prefix is missing or a preamble is present.
     */
    private void validateAllPrefixesPresent(ArgumentMultimap argMultimap) throws ParseException {
        boolean hasAllPrefixes = argMultimap.getValue(PREFIX_FACILITY).isPresent()
                && argMultimap.getValue(PREFIX_DATE).isPresent()
                && argMultimap.getValue(PREFIX_CONTRACTOR_INDEX).isPresent();

        if (!hasAllPrefixes || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddtCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Parses and validates the facility name from the argument map.
     *
     * @param argMultimap The tokenized argument map.
     * @return The trimmed facility name.
     * @throws ParseException If the facility name is empty or exceeds 50 characters.
     */
    private String parseFacility(ArgumentMultimap argMultimap) throws ParseException {
        String facility = argMultimap.getValue(PREFIX_FACILITY).get().trim();
        if (facility.isEmpty() || facility.length() > MaintenanceTask.MAX_FACILITY_LENGTH) {
            throw new ParseException("Facility must be between 1 and 50 characters.");
        }
        return facility;
    }

    /**
     * Parses and validates the date string from the argument map.
     *
     * @param argMultimap The tokenized argument map.
     * @return A {@code LocalDate}.
     * @throws ParseException If the date format is invalid.
     */
    private LocalDate parseDate(ArgumentMultimap argMultimap) throws ParseException {
        String dateStr = argMultimap.getValue(PREFIX_DATE).get().trim();
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            throw new ParseException("Date must be in YYYY-MM-DD format and be a valid date.");
        }
    }

    /**
     * Parses and validates the contractor index from the argument map.
     *
     * @param argMultimap The tokenized argument map.
     * @return A valid {@code Index} for the contractor.
     * @throws ParseException If the value is not a positive integer.
     */
    private Index parseContractorIndex(ArgumentMultimap argMultimap) throws ParseException {
        try {
            return ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CONTRACTOR_INDEX).get());
        } catch (ParseException e) {
            throw new ParseException("Contractor index must be a positive integer.");
        }
    }
}
