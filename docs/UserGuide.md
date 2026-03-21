---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# EstateContacts User Guide

EstateContacts is a **desktop app for managing contacts, optimized for use via a  Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, EstateContacts can get your contact management tasks done faster than traditional GUI apps.

---
## Table of Contents

- [Quick start](#quick-start)
- [Features](#features)
- [FAQ](#faq)
- [Known issues](#known-issues)
- [Command summary](#command-summary)

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `listc` : Lists all contacts.

   * `addc n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01 s/Plumbing` : Adds a contact named `John Doe` to the Address Book.

   * `delc 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `addc n/NAME`, `NAME` is a parameter which can be used as `addc n/John Doe`.

* Items in square brackets are optional.<br>
  e.g. `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</box>

### Contractor features

---

### Adding a contractor : `add`

Adds a contractor to the address book.

Format: `addc n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS s/SERVICE [t/TAG]…​`

<box type="tip" seamless>

**Tip:** A contractor can have any number of tags (including 0)
</box>

Examples:
* `addc n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01 s/Plumbing`
* `addc n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 s/Electrical t/criminal`

### Listing all contractors : `listc`

Shows a list of all contractors in the address book.

Format: `listc`

### Locating contractors by name : `findc`

Finds contractors whose names contain any of the given keywords.

Format: `findc KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Contractors matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `findc John` returns `john` and `John Doe`
* `findc alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Deleting a contractor : `delete`

Deletes the specified contractor from the address book.

Format: `delc INDEX`

* Deletes the contractor at the specified `INDEX`.
* The index refers to the index number shown in the displayed contractor list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `listc` followed by `delc 2` deletes the 2nd contractor in the address book.
* `findc Betsy` followed by `delc 1` deletes the 1st contractor in the results of the `findc` command.

### Maintenance task features

---

### Adding a task : `addt`

Adds a maintenance task to the address book.

Format: `addt f/FACILITY d/DATE (YYYY-MM-DD) c/CONTRACTOR_INDEX`

Examples:
* `addt f/Sports Hall d/2026-12-01 c/2`
* `addt f/Function Room d/2026-06-20 c/4`

### Listing all tasks : `listt`

Shows a list of all tasks in the address book.

Format: `listt`

### Deleting a task : `deletet`

Deletes the specified task from the address book.

Format: `delt INDEX`

* Deletes the task at the specified `INDEX`.
* The index refers to the index number shown in the displayed maintenance tasklist.



### General features

---

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`




### Saving the data

EstateContacts data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

EstateContacts data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action     | Format, Examples
-----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------
**Add**    | `addc n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS s/SERVICE [t/TAG]…​` <br> e.g., `addc n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 s/Plumbing t/friend t/colleague`
**Clear**  | `clear`
**Delete** | `delc INDEX`<br> e.g., `delete 3`
**Find**   | `findc KEYWORD [MORE_KEYWORDS]`<br> e.g., `findc James Jake`
**List**   | `listc`
**Help**   | `help`
