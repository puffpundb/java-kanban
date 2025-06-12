package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;


class FileBackedTaskManagerTest {
    public File filePath;
    public static FileBackedTaskManager taskManager;

    @BeforeEach
    public void beforeEach() throws IOException {
        filePath = File.createTempFile("test", ".csv");

        taskManager = Managers.getFileBackedTaskManager(filePath.toPath());

        taskManager.createNewTask(new Task("0", "0"));
        taskManager.createNewTask(new Task("1", "1"));
        taskManager.createNewTask(new Task("2", "2"));
        taskManager.createNewEpic(new Epic("3", "3"));
        taskManager.createNewSubTask(new SubTask("4", "4", 3));
        taskManager.createNewEpic(new Epic("5", "5"));
        taskManager.createNewSubTask(new SubTask("6", "6", 5));
        taskManager.createNewSubTask(new SubTask("7", "7", 5));
    }

    @Test
    public void shouldTaskManagerGetAllTask() {
        ArrayList<Task> expectedList = new ArrayList<>();
        expectedList.add(taskManager.getTaskById(0));
        expectedList.add(taskManager.getTaskById(1));
        expectedList.add(taskManager.getTaskById(2));

        Assertions.assertEquals(expectedList, taskManager.getAllTasks());
    }

    @Test
    public void shouldTaskManagerGetAllEpics() {
        ArrayList<Epic> expectedEpicList = new ArrayList<>();
        expectedEpicList.add(taskManager.getEpicById(3));
        expectedEpicList.add(taskManager.getEpicById(5));

        Assertions.assertEquals(expectedEpicList, taskManager.getAllEpics());
    }

    @Test
    public void shouldTaskManagerGetAllSubTasks() {
        ArrayList<SubTask> expectedSubTaskList = new ArrayList<>();
        expectedSubTaskList.add(taskManager.getSubTaskById(4));
        expectedSubTaskList.add(taskManager.getSubTaskById(6));
        expectedSubTaskList.add(taskManager.getSubTaskById(7));

        Assertions.assertEquals(expectedSubTaskList, taskManager.getAllSubTasks());
    }

    @Test
    public void shouldTaskManagerRightUpdateTask() {
        Task expectedTask = new Task("10", "10");
        expectedTask.setId(0);

        Task currentTask = taskManager.getTaskById(0);
        currentTask.setTitle("10");
        currentTask.setDescription("10");
        taskManager.updateTask(currentTask);

        Assertions.assertEquals(expectedTask, taskManager.getTaskById(0));
    }

    @Test
    public void shouldTaskManagerRightUpdateEpic() {
        Epic expectedEpic = new Epic("11", "11");
        expectedEpic.setId(3);
        expectedEpic.putSubsId(4);

        Epic currentEpic = taskManager.getEpicById(3);
        currentEpic.setTitle("11");
        currentEpic.setDescription("11");
        taskManager.updateEpic(currentEpic);

        Assertions.assertEquals(expectedEpic, taskManager.getEpicById(3));
    }

    @Test
    public void shouldTaskManagerRightUpdateSubTask() {
        SubTask expectedSubTask = new SubTask("12", "12", 3);
        expectedSubTask.setId(4);

        SubTask currentSubtask = taskManager.getSubTaskById(4);
        currentSubtask.setTitle("12");
        currentSubtask.setDescription("12");
        taskManager.updateSubTask(currentSubtask);

        Assertions.assertEquals(expectedSubTask, taskManager.getSubTaskById(4));
    }

    @Test
    public void shouldTaskManagerDeleteTaskById() {
        TaskManager newTaskManager = Managers.getDefault();
        newTaskManager.createNewTask(new Task("0", "0"));
        newTaskManager.deleteTask(0);

        Assertions.assertEquals(0, newTaskManager.getAllTasks().size());
    }

    @Test
    public void shouldTaskManagerDeleteEpicById() {
        TaskManager newTaskManager = Managers.getDefault();
        newTaskManager.createNewEpic(new Epic("1", "1"));
        newTaskManager.deleteEpic(0);

        Assertions.assertEquals(0, newTaskManager.getAllEpics().size());
    }

    @Test
    public void shouldTaskManagerDeleteSubById() {
        TaskManager newTaskManager = Managers.getDefault();
        newTaskManager.createNewEpic(new Epic("1", "1"));
        newTaskManager.createNewSubTask(new SubTask("2", "2", 0));
        newTaskManager.deleteSubTask(1);

        Assertions.assertEquals(0, newTaskManager.getAllSubTasks().size());
    }

    @Test
    public void shouldTaskManagerGetEpicSubs() {
        ArrayList<SubTask> expectedSubList = new ArrayList<>();
        expectedSubList.add(taskManager.getSubTaskById(6));
        expectedSubList.add(taskManager.getSubTaskById(7));

        Assertions.assertEquals(expectedSubList, taskManager.getEpicSubs(5));
    }

    @Test
    public void shouldTaskWithOneIdDontGiveAConflict() {
        Task taskToUpdate = new Task("1", "1");
        taskToUpdate.setId(0);
        taskManager.updateTask(taskToUpdate);

        Assertions.assertEquals(taskToUpdate, taskManager.getTaskById(0));
    }

    @Test
    public void shouldTaskManagerClearAllTasks() {
        taskManager.clearAllTasks();

        Assertions.assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    public void shouldTaskManagerClearAllEpics() {
        taskManager.clearAllEpics();

        Assertions.assertEquals(0, taskManager.getAllEpics().size());
    }

    @Test
    public void shouldTaskManagerCleaAllSubs() {
        taskManager.clearAllSubTasks();

        Assertions.assertEquals(0, taskManager.getAllSubTasks().size());
    }

    @Test
    public void shouldTaskManagerRightSaveTaskInFile() {
        Task rightTask = new Task("0", "0");
        rightTask.setId(0);

        FileBackedTaskManager managerForTest = FileBackedTaskManager.loadFromFile(filePath.toPath());
        Task taskToAssert = managerForTest.getTaskById(0);

        Assertions.assertEquals(rightTask, taskToAssert);
    }

    @Test
    public void shouldTaskManagerRightSaveEpicInFile() {
        Epic rightEpic = new Epic("3", "3");
        rightEpic.setId(3);

        FileBackedTaskManager managerForTest = FileBackedTaskManager.loadFromFile(filePath.toPath());
        Epic epicForAssert = managerForTest.getEpicById(3);

        Assertions.assertEquals(rightEpic, epicForAssert);
    }

    @Test
    public void shouldTaskManagerRightSaveSubInFile() {
        SubTask rightSub = new SubTask("4", "4", 3);
        rightSub.setId(4);

        FileBackedTaskManager managerForTest = FileBackedTaskManager.loadFromFile(filePath.toPath());
        SubTask subForAssert = managerForTest.getSubTaskById(4);

        Assertions.assertEquals(rightSub, subForAssert);
    }

    @Test
    public void shouldTaskManagerRightSaveEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("testEmpty", ".csv");
        FileBackedTaskManager emptyManager = Managers.getFileBackedTaskManager(emptyFile.toPath());
        emptyManager.save();

        Assertions.assertEquals(0, Files.size(emptyFile.toPath()));
    }

    @Test
    public void shouldTaskManagerRightLoadEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("testEmpty", ".csv");
        FileBackedTaskManager emptyManager = Managers.getFileBackedTaskManager(emptyFile.toPath());
        emptyManager.save();

        FileBackedTaskManager secondEmptyManager = FileBackedTaskManager.loadFromFile(emptyFile.toPath());

        Assertions.assertEquals(0, secondEmptyManager.getHistory().size());
    }
}