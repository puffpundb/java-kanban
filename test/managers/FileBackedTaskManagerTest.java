package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Task;

import java.io.IOException;
import java.nio.file.Files;


public class FileBackedTaskManagerTest extends TaskManagerTest<TaskManager> {
    @Test
    public void shouldTaskManagerRightSaveTaskInFile() {
        Task assertTask = taskManager.getTaskById(0);

        FileBackedTaskManager managerForTest = FileBackedTaskManager.loadFromFile(filePath.toPath());
        Task taskToAssert = managerForTest.getTaskById(0);

        Assertions.assertEquals(assertTask, taskToAssert);
    }

    @Test
    public void shouldTaskManagerRightSaveEpicInFile() {
        Epic rightEpic = taskManager.getEpicById(3).copy();

        FileBackedTaskManager managerForTest = FileBackedTaskManager.loadFromFile(filePath.toPath());
        Epic epicForAssert = managerForTest.getEpicById(3);

        Assertions.assertEquals(rightEpic, epicForAssert);
    }

    @Test
    public void shouldTaskManagerRightSaveSubInFile() {
        FileBackedTaskManager testManager = FileBackedTaskManager.loadFromFile(filePath.toPath());

        Assertions.assertEquals(taskManager.getSubTaskById(4), testManager.getSubTaskById(4));
    }

    @Test
    public void shouldTaskManagerRightSaveEmptyFile() throws IOException {
        taskManager.clearAllSubTasks();
        taskManager.clearAllTasks();
        taskManager.clearAllEpics();

        Assertions.assertEquals(1, Files.readAllLines(filePath.toPath()).size());
    }

    @Test
    public void shouldTaskManagerRightLoadEmptyFile() {
        taskManager.clearAllSubTasks();
        taskManager.clearAllTasks();
        taskManager.clearAllEpics();

        FileBackedTaskManager secondEmptyManager = FileBackedTaskManager.loadFromFile(filePath.toPath());

        Assertions.assertEquals(taskManager.getAllTasks(), secondEmptyManager.getAllTasks());
        Assertions.assertEquals(taskManager.getAllEpics(), secondEmptyManager.getAllEpics());
        Assertions.assertEquals(taskManager.getAllSubTasks(), secondEmptyManager.getAllSubTasks());
    }

    @Test
    public void shouldTaskRightLoadFromFile() {
        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(filePath.toPath());

        Assertions.assertEquals(taskManager.getAllTasks(), loadedTaskManager.getAllTasks());
        Assertions.assertEquals(taskManager.getAllEpics(), loadedTaskManager.getAllEpics());
        Assertions.assertEquals(taskManager.getAllSubTasks(), loadedTaskManager.getAllSubTasks());
    }

    @Test
    public void shouldTaskManagerRightClearAllTasks() {
        taskManager.clearAllTasks();
        taskManager.clearAllEpics();
        taskManager.clearAllSubTasks();

        FileBackedTaskManager testManager = Managers.getFileBackedTaskManager(filePath.toPath());

        Assertions.assertEquals(taskManager.getAllTasks(), testManager.getAllTasks());
        Assertions.assertEquals(taskManager.getAllEpics(), testManager.getAllEpics());
        Assertions.assertEquals(taskManager.getAllSubTasks(), testManager.getAllSubTasks());
    }
}