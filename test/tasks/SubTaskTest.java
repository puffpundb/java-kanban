package tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static managers.FileBackedTaskManagerTest.date;
import static managers.FileBackedTaskManagerTest.time;

class SubTaskTest {
    public static SubTask subTask0;
    public static SubTask subTask1;

    @BeforeAll
    public static void BeforeAll() {
        subTask0 = new SubTask("0", "0", 0, Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(date, time));
        subTask1 = new SubTask("0", "0", 0, Duration.of(1, ChronoUnit.HOURS), LocalDateTime.of(date, time));
    }

    @Test
    public void shouldSubTasksEquals() {
        Assertions.assertEquals(subTask0, subTask1);
    }

    @Test
    public void shouldSubTasksEqualsTitle() {
        Assertions.assertEquals(subTask0.getTitle(), subTask1.getTitle());
    }

    @Test
    public void shouldSubTasksEqualsDescription() {
        Assertions.assertEquals(subTask0.getDescription(), subTask1.getDescription());
    }

    @Test
    public void shouldSubTasksEqualsId() {
        subTask0.setId(0);
        subTask1.setId(0);

        Assertions.assertEquals(subTask0.getId(), subTask1.getId());
    }

    @Test
    public void shouldSubTasksEqualsStatus() {
        Assertions.assertEquals(subTask0.getStatus(), subTask1.getStatus());
    }

    @Test
    public void shouldSubTasksEqualsEpicId() {
        Assertions.assertEquals(subTask0.getEpicId(), subTask1.getEpicId());
    }
}