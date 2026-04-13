package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTRACTOR_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FACILITY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EdittCommand;
import seedu.address.logic.commands.EdittCommand.EditTaskDescriptor;


/**
 * Contains unit tests for {@code EdittCommandParser}.
 */
public class EdittCommandParserTest {

    private final EdittCommandParser parser = new EdittCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        LocalDate futureDate = LocalDate.now().plusDays(10);
        String userInput = "1 "
                + PREFIX_FACILITY + "Sports Hall "
                + PREFIX_DATE + futureDate + " "
                + PREFIX_CONTRACTOR_INDEX + "2";

        EditTaskDescriptor descriptor = new EditTaskDescriptor();
        descriptor.setFacility("Sports Hall");
        descriptor.setDate(futureDate);
        descriptor.setContractorIndex(Index.fromOneBased(2));

        assertParseSuccess(parser, userInput, new EdittCommand(Index.fromOneBased(1), descriptor));
    }

    @Test
    public void parse_someFieldsPresent_success() {
        EditTaskDescriptor descriptor = new EditTaskDescriptor();
        descriptor.setFacility("Swimming Pool");

        assertParseSuccess(parser, "1 " + PREFIX_FACILITY + "Swimming Pool",
                new EdittCommand(Index.fromOneBased(1), descriptor));
    }

    @Test
    public void parse_missingParts_failure() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EdittCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "1", EdittCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertParseFailure(parser, "a " + PREFIX_FACILITY + "Sports Hall",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EdittCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_repeatedPrefix_failure() {
        String userInput = "1 " + PREFIX_FACILITY + "Sports Hall " + PREFIX_FACILITY + "Swimming Pool";
        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_FACILITY));
    }

    @Test
    public void parse_invalidFacility_failure() {
        assertParseFailure(parser, "1 " + PREFIX_FACILITY,
                "Facility must be between 1 and 50 characters.");
    }

    @Test
    public void parse_invalidDate_failure() {
        assertParseFailure(parser, "1 " + PREFIX_DATE + "2026-13-01",
                "Date must be in YYYY-MM-DD format and be a valid date.");
    }

    @Test
    public void parse_invalidContractorIndex_failure() {
        assertParseFailure(parser, "1 " + PREFIX_CONTRACTOR_INDEX + "abc",
                "Contractor index must be a positive integer.");
    }
}
