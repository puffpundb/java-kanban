package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.Task;

class HistoryManagerTest {
    public static TaskManager taskManager;

    @BeforeAll
    public static void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void shouldHistorySaveTaskCopy() {
        taskManager.createNewTask(new Task("0", "0"));
        String expectedTitle = "1";
        taskManager.getTaskById(0).setTitle("1");
        String taskTitleFromHistory = taskManager.getHistory().getFirst().getTitle();

        Assertions.assertNotEquals(expectedTitle, taskTitleFromHistory);
    }
}