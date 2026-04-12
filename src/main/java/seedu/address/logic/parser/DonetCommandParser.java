package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DonetCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DonetCommand object.
 */
public class DonetCommandParser implements Parser<DonetCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DonetCommand
     * and returns a DonetCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public DonetCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DonetCommand(index);
        } catch (ParseException pe) {
            if (pe.getMessage().equals(ParserUtil.MESSAGE_INVALID_INDEX)) {
                throw pe;
            }
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DonetCommand.MESSAGE_USAGE), pe);
        }
    }
}
