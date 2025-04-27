import managers.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

class MainTest {

    public static InMemoryTaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
    }

    @Test
    public void shouldTaskByIdEqualTaskById() {
        Task task0 = new Task("0", "0");
        task0.setId(0);
        Task task1 = new Task("1", "1");
        task1.setId(0);

        Assertions.assertEquals(task0.getId(), task1.getId());
    }

    @Test
    public void shouldExtendsTaskEquals() {
        Task task0 = new SubTask("0", "0", 1);
        task0.setId(0);
        Task task1 = new Epic("1", "1");
        task1.setId(0);

        Assertions.assertEquals(task0.getId(), task1.getId());
    }

    @Test
    public void shouldManagerDontConstructNullObjects() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        InMemoryHistoryManager inMemoryHistoryManager = (InMemoryHistoryManager) Managers.getDefaultHistory();

        Assertions.assertNotNull(inMemoryTaskManager);
        Assertions.assertNotNull(inMemoryHistoryManager);
    }

    @Test
    public void shouldTaskManagerCreateAndFindTasksById() {
        taskManager.createNewTask(new Task("0", "0"));
        taskManager.createNewEpic(new Epic("1", "1"));
        taskManager.createNewSubTask(new SubTask("2", "2", 1));

        Assertions.assertNotNull(taskManager.getTaskById(0));
        Assertions.assertNotNull(taskManager.getEpicById(1));
        Assertions.assertNotNull(taskManager.getSubTaskById(2));
    }

    @Test
    public void shouldTaskWithOneIdDontGiveAConflict() {
        taskManager.createNewTask(new Task("0", "0"));
        Task taskToUpdate = new Task("1", "1");
        taskToUpdate.setId(0);
        taskManager.updateTask(taskToUpdate);

        Assertions.assertEquals(taskToUpdate, taskManager.getTaskById(0));
    }

    @Test
    public void shouldTaskSaveHisField() {
        taskManager.createNewTask(new Task("0", "0"));

        Assertions.assertEquals(0, taskManager.getTaskById(0).getId());
        Assertions.assertEquals("0", taskManager.getTaskById(0).getTitle());
        Assertions.assertEquals("0", taskManager.getTaskById(0).getDescription());
        Assertions.assertEquals(Status.NEW, taskManager.getTaskById(0).getStatus());
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