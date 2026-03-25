package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FACILITY;

import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new HistoryCommand object.
 */
public class HistoryCommandParser implements Parser<HistoryCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the
     * HistoryCommand
     * and returns a HistoryCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected
     *                        format
     */
    public HistoryCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_FACILITY);
        if (argMultimap.getValue(PREFIX_FACILITY).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HistoryCommand.MESSAGE_USAGE));
        }
        String facilityName = argMultimap.getValue(PREFIX_FACILITY).get();
        return new HistoryCommand(facilityName);
    }
}
