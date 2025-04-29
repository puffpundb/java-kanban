package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;

class TaskManagerTest {
    public static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void shouldTaskManagerGetAll() { // Совсем не уверен в реализации тестов.. Решил, что раз они все с одного поля ягоды, то можно их в одном тесте проверить, также и далее
        taskManager.createNewTask(new Task("0", "0"));
        taskManager.createNewTask(new Task("1", "1"));
        taskManager.createNewTask(new Task("2", "2"));
        taskManager.createNewEpic(new Epic("3", "3"));
        taskManager.createNewSubTask(new SubTask("4", "4", 3));
        taskManager.createNewEpic(new Epic("5", "5"));
        taskManager.createNewSubTask(new SubTask("6", "6", 5));
        taskManager.createNewSubTask(new SubTask("7", "7", 5));

        ArrayList<Task> expectedList = new ArrayList<>();
        expectedList.add(taskManager.getTaskById(0));
        expectedList.add(taskManager.getTaskById(1));
        expectedList.add(taskManager.getTaskById(2));

        ArrayList<Epic> expectedEpicList = new ArrayList<>();
        expectedEpicList.add(taskManager.getEpicById(3));
        expectedEpicList.add(taskManager.getEpicById(5));

        ArrayList<SubTask> expectedSubTaskList = new ArrayList<>();
        expectedSubTaskList.add(taskManager.getSubTaskById(4));
        expectedSubTaskList.add(taskManager.getSubTaskById(6));
        expectedSubTaskList.add(taskManager.getSubTaskById(7));

        Assertions.assertEquals(expectedList, taskManager.getAllTasks());
        Assertions.assertEquals(expectedEpicList, taskManager.getAllEpics());
        Assertions.assertEquals(expectedSubTaskList, taskManager.getAllSubTasks());
    }

    @Test
    public void shouldTaskManagerClearAll() {
        taskManager.createNewTask(new Task("0", "0"));
        taskManager.createNewTask(new Task("1", "1"));
        taskManager.createNewTask(new Task("2", "2"));
        taskManager.createNewEpic(new Epic("0", "0"));
        taskManager.createNewSubTask(new SubTask("1", "1", 0));
        taskManager.createNewEpic(new Epic("2", "2"));
        taskManager.createNewSubTask(new SubTask("3", "3", 2));
        taskManager.createNewSubTask(new SubTask("4", "4", 2));

        taskManager.clearAllTasks();
        taskManager.clearAllSubTasks();
        taskManager.clearAllEpics();

        Assertions.assertEquals(0, taskManager.getAllTasks().size());
        Assertions.assertEquals(0, taskManager.getAllEpics().size());
        Assertions.assertEquals(0, taskManager.getAllSubTasks().size());
    }

    @Test
    public void shouldTaskManagerRightUpdateAll() {
        taskManager.createNewTask(new Task("0", "0"));
        taskManager.createNewEpic(new Epic("1", "1"));
        taskManager.createNewSubTask(new SubTask("2", "2", 1));

        Task expectedTask = new Task("10", "10");
        expectedTask.setId(0);

        Task currentTask = taskManager.getTaskById(0);
        currentTask.setTitle("10");
        currentTask.setDescription("10");
        taskManager.updateTask(currentTask);


        Epic expectedEpic = new Epic("11", "11");
        expectedEpic.setId(1);
        expectedEpic.putSubsId(2);

        Epic currentEpic = taskManager.getEpicById(1);
        currentEpic.setTitle("11");
        currentEpic.setDescription("11");
        taskManager.updateTask(currentTask);


        SubTask expectedSubTask = new SubTask("12", "12", 1);
        expectedSubTask.setId(2);

        SubTask currentSubtask = taskManager.getSubTaskById(2);
        currentSubtask.setTitle("12");
        currentSubtask.setDescription("12");
        taskManager.updateSubTask(currentSubtask);

        Assertions.assertEquals(expectedTask, taskManager.getTaskById(0));
        Assertions.assertEquals(expectedEpic, taskManager.getEpicById(1));
        Assertions.assertEquals(expectedSubTask, taskManager.getSubTaskById(2));
    }

    @Test
    public void shouldTaskManagerDeleteById() {
        taskManager.createNewTask(new Task("0", "0"));
        taskManager.createNewEpic(new Epic("1", "1"));
        taskManager.createNewSubTask(new SubTask("2", "2", 1));

        taskManager.deleteTask(0);
        taskManager.deleteSubTask(2);
        taskManager.deleteEpic(1);

        Assertions.assertEquals(0, taskManager.getAllTasks().size());
        Assertions.assertEquals(0, taskManager.getAllEpics().size());
        Assertions.assertEquals(0, taskManager.getAllSubTasks().size());
    }

    @Test
    public void shouldTaskManagerGetEpicSubs() {
        taskManager.createNewEpic(new Epic("0", "0"));
        taskManager.createNewSubTask(new SubTask("1", "1", 0));

        ArrayList<SubTask> expectedSubList = new ArrayList<>();
        expectedSubList.add(taskManager.getSubTaskById(1));

        Assertions.assertEquals(expectedSubList, taskManager.getEpicSubs(0));
    }

    @Test
    public void shouldTaskWithOneIdDontGiveAConflict() {
        taskManager.createNewTask(new Task("0", "0"));
        Task taskToUpdate = new Task("1", "1");
        taskToUpdate.setId(0);
        taskManager.updateTask(taskToUpdate);

        Assertions.assertEquals(taskToUpdate, taskManager.getTaskById(0));
    }
}