package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeltCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeltCommand object
 */
public class DeltCommandParser implements Parser<DeltCommand> {


    /**
     * Parses the given {@code String} of arguments in the context
     * of the DeltCommand and returns a DeltCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */

    public DeltCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeltCommand(index);
        } catch (ParseException pe) {
            if (pe.getMessage().equals(ParserUtil.MESSAGE_INVALID_INDEX)) {
                throw pe;
            }
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeltCommand.MESSAGE_USAGE), pe);
        }
    }
}
