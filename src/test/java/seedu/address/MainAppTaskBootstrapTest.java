package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.MaintenanceTaskList;
import seedu.address.model.util.SampleContractorDataUtil;
import seedu.address.model.util.SampleTaskDataUtil;
import seedu.address.storage.Storage;

public class MainAppTaskBootstrapTest {

    @Test
    public void loadTaskList_missingTaskFile_usesSampleTasks() throws Exception {
        ModelManager modelManager = new ModelManager(SampleContractorDataUtil.getSampleAddressBook(), new UserPrefs());
        Storage storage = new StorageStub(Optional.empty(), false);

        invokeLoadTaskList(storage, modelManager);

        assertEquals(SampleTaskDataUtil.getSampleTaskList().size(), modelManager.getMaintenanceTaskList().size());
        assertEquals("North Tower Lift Lobby", modelManager.getMaintenanceTaskList().getTasks().get(0).getFacility());
    }

    @Test
    public void loadTaskList_existingTaskFile_usesSavedTasks() throws Exception {
        ModelManager modelManager = new ModelManager(SampleContractorDataUtil.getSampleAddressBook(), new UserPrefs());
        MaintenanceTaskList savedTasks = SampleTaskDataUtil.getSampleTaskList();
        savedTasks.removeTask(savedTasks.size() - 1); // make it clearly different from default sample size
        Storage storage = new StorageStub(Optional.of(savedTasks), false);

        invokeLoadTaskList(storage, modelManager);

        assertEquals(savedTasks.size(), modelManager.getMaintenanceTaskList().size());
    }

    @Test
    public void loadTaskList_dataLoadingException_startsWithEmptyTaskList() throws Exception {
        ModelManager modelManager = new ModelManager(SampleContractorDataUtil.getSampleAddressBook(), new UserPrefs());
        Storage storage = new StorageStub(Optional.empty(), true);

        invokeLoadTaskList(storage, modelManager);

        assertTrue(modelManager.getMaintenanceTaskList().isEmpty());
    }

    private void invokeLoadTaskList(Storage storage, ModelManager modelManager) throws Exception {
        Method method = MainApp.class.getDeclaredMethod("loadTaskList", Storage.class, ModelManager.class);
        method.setAccessible(true);
        method.invoke(new MainApp(), storage, modelManager);
    }

    private static class StorageStub implements Storage {
        private final Optional<MaintenanceTaskList> taskListResult;
        private final boolean shouldThrowOnReadTaskList;

        StorageStub(Optional<MaintenanceTaskList> taskListResult, boolean shouldThrowOnReadTaskList) {
            this.taskListResult = taskListResult;
            this.shouldThrowOnReadTaskList = shouldThrowOnReadTaskList;
        }

        @Override
        public Optional<MaintenanceTaskList> readTaskList() throws DataLoadingException {
            if (shouldThrowOnReadTaskList) {
                throw new DataLoadingException(new Exception("simulated read failure"));
            }
            return taskListResult;
        }

        @Override
        public Optional<MaintenanceTaskList> readTaskList(Path filePath) {
            return taskListResult;
        }

        @Override
        public Path getTaskListFilePath() {
            return Path.of("tasklist.json");
        }

        @Override
        public void saveTaskList(MaintenanceTaskList taskList) {
            throw new UnsupportedOperationException("not needed for this test");
        }

        @Override
        public void saveTaskList(MaintenanceTaskList taskList, Path filePath) {
            throw new UnsupportedOperationException("not needed for this test");
        }

        @Override
        public Path getUserPrefsFilePath() {
            throw new UnsupportedOperationException("not needed for this test");
        }

        @Override
        public Optional<UserPrefs> readUserPrefs() {
            throw new UnsupportedOperationException("not needed for this test");
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new UnsupportedOperationException("not needed for this test");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new UnsupportedOperationException("not needed for this test");
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook() {
            throw new UnsupportedOperationException("not needed for this test");
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) {
            throw new UnsupportedOperationException("not needed for this test");
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook) {
            throw new UnsupportedOperationException("not needed for this test");
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) {
            throw new UnsupportedOperationException("not needed for this test");
        }
    }
}
