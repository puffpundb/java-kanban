package managers;


import exceptions.ManagerSaveException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

class InMemoryTaskManagerTest extends TaskManagerTest<TaskManager> {
    @BeforeEach
    public void beforeEach() throws IOException {
        filePath = File.createTempFile("test", ".csv");

        taskManager = Managers.getDefault();

        taskManager.createNewTask(new Task("0", "0", Duration.ofMinutes(1), LocalDateTime.of(2020, 1, 21, 10, 0)));
        taskManager.createNewTask(new Task("1", "1", Duration.ofDays(1), LocalDateTime.of(2021, 1, 21, 10, 0)));
        taskManager.createNewTask(new Task("2", "2", Duration.ofDays(7), LocalDateTime.of(2022, 1, 21, 10, 0)));
        taskManager.createNewEpic(new Epic("3", "3"));
        taskManager.createNewSubTask(new SubTask("4", "4", 3, Duration.ofHours(1), LocalDateTime.of(2023, 1, 21, 10, 0)));
        taskManager.createNewEpic(new Epic("5", "5"));
        taskManager.createNewSubTask(new SubTask("6", "6", 5, Duration.ofMinutes(1), LocalDateTime.of(2024, 1, 21, 10, 0)));
        taskManager.createNewSubTask(new SubTask("7", "7", 5, Duration.ofMinutes(1), LocalDateTime.of(2025, 1, 21, 10, 0)));
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
        Task expectedTask = new Task("10", "10", Duration.ofMinutes(1), LocalDateTime.of(2020, 1, 21, 10, 0));
        expectedTask.setId(0);

        Task currentTask = taskManager.getTaskById(0);
        currentTask.setTitle("10");
        currentTask.setDescription("10");
        currentTask.setDuration(Duration.ofMinutes(1));
        taskManager.updateTask(currentTask);

        Assertions.assertEquals(expectedTask, taskManager.getTaskById(0));
    }

    @Test
    public void shouldTaskManagerRightUpdateEpic() {
        Epic expectedEpic = taskManager.getEpicById(3).copy();
        expectedEpic.setTitle("11");
        expectedEpic.setDescription("11");

        Epic currentEpic = taskManager.getEpicById(3);
        currentEpic.setTitle("11");
        currentEpic.setDescription("11");
        taskManager.updateEpic(currentEpic);

        Assertions.assertEquals(expectedEpic, taskManager.getEpicById(3));
    }

    @Test
    public void shouldTaskManagerRightUpdateSubTask() {
        SubTask expectedSubTask = new SubTask("12", "12", 3, Duration.of(1, ChronoUnit.MINUTES), LocalDateTime.of(2023, 1, 21, 10, 0));
        expectedSubTask.setId(4);

        SubTask currentSubtask = taskManager.getSubTaskById(4);
        currentSubtask.setTitle("12");
        currentSubtask.setDescription("12");
        currentSubtask.setDuration(Duration.of(1, ChronoUnit.MINUTES));
        taskManager.updateSubTask(currentSubtask);

        Assertions.assertEquals(expectedSubTask, taskManager.getSubTaskById(4));
    }

    @Test
    public void shouldTaskManagerDeleteTaskById() {
        TaskManager newTaskManager = Managers.getDefault();
        newTaskManager.createNewTask(new Task("0", "0", Duration.of(1, ChronoUnit.MINUTES), LocalDateTime.now()));
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
        newTaskManager.createNewSubTask(new SubTask("2", "2", 0, Duration.of(1, ChronoUnit.HOURS), LocalDateTime.now()));
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
        Task taskToUpdate = new Task("0", "0", Duration.ofMinutes(3), LocalDateTime.of(2020, 1, 21, 10, 0));
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
    public void shouldTaskManagerRightUpdateEpicStatusWithOneDoneSub() {
        SubTask subForTest = taskManager.getSubTaskById(6);
        subForTest.setStatus(Status.DONE);
        taskManager.updateSubTask(subForTest);

        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(5).getStatus());
    }

    @Test
    public void shouldTaskManagerRightUpdateEpicStatusWithAllDoneSub() {
        SubTask subForTest = taskManager.getSubTaskById(6);
        subForTest.setStatus(Status.DONE);
        taskManager.updateSubTask(subForTest);

        subForTest = taskManager.getSubTaskById(7);
        subForTest.setStatus(Status.DONE);
        taskManager.updateSubTask(subForTest);

        Assertions.assertEquals(Status.DONE, taskManager.getEpicById(5).getStatus());
    }

    @Test
    public void shouldTaskManagerRightUpdateEpicStatusWithInProgressSubs() {
        SubTask subForTest = taskManager.getSubTaskById(6);
        subForTest.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubTask(subForTest);

        subForTest = taskManager.getSubTaskById(7);
        subForTest.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubTask(subForTest);

        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(5).getStatus());
    }

    @Test
    public void shouldTaskManagerRightCalculatesTimePeriodsIntersectedFull() {
        Assertions.assertThrows(ManagerSaveException.class, () -> {
            Task taskForTest = taskManager.getTaskById(1);
            taskForTest.setStartTime(LocalDateTime.of(2020, 1, 21, 10, 0));
            taskManager.updateTask(taskForTest);
        });
    }

    @Test
    public void shouldTaskManagerRightCalculatesTimePeriodsIntersectedFirstInSecond() {
        Assertions.assertThrows(ManagerSaveException.class, () -> {
            Task taskForTest = taskManager.getTaskById(1);
            taskForTest.setStartTime(LocalDateTime.of(2022, 1, 22, 10, 0));
            taskManager.updateTask(taskForTest);
        });
    }

    @Test
    public void shouldTaskManagerRightCalculatesTimePeriodsIntersectedHalf() {
        Assertions.assertThrows(ManagerSaveException.class, () -> {
            Task taskForTest = taskManager.getTaskById(1);
            taskForTest.setDuration(Duration.ofDays(10));
            taskForTest.setStartTime(LocalDateTime.of(2022, 1, 24, 10, 0));
            taskManager.updateTask(taskForTest);
        });
    }

    @Test
    public void shouldTaskManagerRightCalculatesTimePeriodsIntersectedHalfSecond() {
        Assertions.assertThrows(ManagerSaveException.class, () -> {
            Task taskForTest = taskManager.getTaskById(1);
            taskForTest.setDuration(Duration.ofDays(10));
            taskForTest.setStartTime(LocalDateTime.of(2022, 1, 19, 10, 0));
            taskManager.updateTask(taskForTest);
        });
    }

    @Test
    public void shouldTaskManagerRightCalculateEpicStartTime() {
        Assertions.assertEquals(taskManager.getSubTaskById(6).getStartTime(), taskManager.getEpicById(5).getStartTime());
    }

    @Test
    public void shouldTaskManagerRightCalculateEpicDuration() {
        Duration rightEpicDuration = Duration.between(taskManager.getSubTaskById(6).getStartTime(), taskManager.getSubTaskById(7).getEndTime());

        Assertions.assertEquals(rightEpicDuration, taskManager.getEpicById(5).getDuration());
    }
}