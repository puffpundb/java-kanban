package managers;


import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

class InMemoryTaskManagerTest extends TaskManagerTest<TaskManager> {
    @BeforeEach
    public void beforeEach() throws IOException {
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
}