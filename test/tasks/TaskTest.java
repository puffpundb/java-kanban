package tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TaskTest {
    public static Task task0;
    public static Task task1;

    @BeforeAll
    public static void BeforeAll() {
        task0 = new Task("0", "0");
        task1 = new Task("0", "0");
    }

    @Test
    public void shouldTasksEquals() {
        Assertions.assertEquals(task0, task1);
    }

    @Test
    public void shouldTasksEqualsTitle() {
        Assertions.assertEquals(task0.getTitle(), task1.getTitle());
    }

    @Test
    public void shouldTasksEqualsDescription() {
        Assertions.assertEquals(task0.getDescription(), task1.getDescription());
    }

    @Test
    public void shouldTasksEqualsStatus() {
        Assertions.assertEquals(task0.getStatus(), task1.getStatus());
    }

    @Test
    public void shouldTasksEqualsId() {
        task0.setId(0);
        task1.setId(0);

        Assertions.assertEquals(task0.getId(), task1.getId());
    }
}