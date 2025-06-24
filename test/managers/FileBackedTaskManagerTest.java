package managers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;


public class FileBackedTaskManagerTest extends TaskManagerTest<TaskManager> {
    public File filePath;

    @BeforeEach
    public void beforeEach() throws IOException {
        filePath = File.createTempFile("test", ".csv");

        taskManager = Managers.getFileBackedTaskManager(filePath.toPath());

        taskManager.createNewTask(new Task("0", "0", Duration.ofMinutes(1), LocalDateTime.of(2020, 1, 21, 10, 0)));
        taskManager.createNewTask(new Task("1", "1", Duration.ofDays(1), LocalDateTime.of(2021, 1, 21, 10, 0)));
        taskManager.createNewTask(new Task("2", "2", Duration.ofDays(7), LocalDateTime.of(2022, 1, 21, 10, 0)));
        taskManager.createNewEpic(new Epic("3", "3"));
        taskManager.createNewSubTask(new SubTask("4", "4", 3, Duration.ofHours(1), LocalDateTime.of(2023, 1, 21, 10, 0)));
        taskManager.createNewEpic(new Epic("5", "5"));
        taskManager.createNewSubTask(new SubTask("6", "6", 5, Duration.ofMinutes(1), LocalDateTime.of(2024, 1, 21, 10, 0)));
        taskManager.createNewSubTask(new SubTask("7", "7", 5, Duration.ofMinutes(1), LocalDateTime.of(2025, 1, 21, 10, 0)));
    }

    @AfterEach
    public void afterEach() {
        if (filePath.exists()) {
            filePath.delete();
        }
    }

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