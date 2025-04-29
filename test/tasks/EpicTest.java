package tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EpicTest {
    public static Epic epic0;
    public static Epic epic1;

    @BeforeAll
    public static void BeforeAll() {
        epic0 = new Epic("0", "0");
        epic1 = new Epic("0", "0");
    }

    @Test
    public void shouldEpicsEquals() {
        Assertions.assertEquals(epic0, epic1);
    }

    @Test
    public void shouldEpicsEqualsTitle() {
        Assertions.assertEquals(epic0.getTitle(), epic1.getTitle());
    }

    @Test
    public void shouldEpicsEqualsDescription() {
        Assertions.assertEquals(epic0.getDescription(), epic1.getDescription());
    }

    @Test
    public void shouldEpicsEqualsId() {
        epic0.setId(0);
        epic1.setId(0);

        Assertions.assertEquals(epic0.getId(), epic1.getId());
    }

    @Test
    public void shouldEpicsEqualsStatus() {
        Assertions.assertEquals(epic0.getStatus(), epic1.getStatus());
    }

    @Test
    public void shouldEpicsEqualsSubs() {
        epic0.putSubsId(0);
        epic1.putSubsId(0);

        Assertions.assertEquals(epic0.getSubsId(), epic1.getSubsId());
    }
}