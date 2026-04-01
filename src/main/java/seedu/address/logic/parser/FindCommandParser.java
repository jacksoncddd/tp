package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVICE;

import java.util.Arrays;
import java.util.List;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.ServiceContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + trimmedArgs, PREFIX_NAME, PREFIX_SERVICE);
        boolean hasNameKeywords = argMultimap.getValue(PREFIX_NAME).isPresent();
        boolean hasServiceKeywords = argMultimap.getValue(PREFIX_SERVICE).isPresent();

        if (!argMultimap.getPreamble().isEmpty() || hasNameKeywords == hasServiceKeywords) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_SERVICE);
        if (hasNameKeywords) {
            String nameValue = argMultimap.getValue(PREFIX_NAME).orElse("").trim();
            if (nameValue.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            List<String> nameKeywords = Arrays.asList(nameValue.split("\\s+"));
            return new FindCommand(new NameContainsKeywordsPredicate(nameKeywords));
        } else {
            String serviceValue = argMultimap.getValue(PREFIX_SERVICE).orElse("").trim();
            if (serviceValue.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            List<String> serviceKeywords = Arrays.asList(serviceValue.split("\\s+"));
            return new FindCommand(new ServiceContainsKeywordsPredicate(serviceKeywords));
        }
    }

}
