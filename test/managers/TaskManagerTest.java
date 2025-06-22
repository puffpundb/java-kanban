package managers;

import org.junit.jupiter.api.AfterEach;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;

abstract class TaskManagerTest<T extends TaskManager> {
    public File filePath;
    public T taskManager;
    public static LocalDate date = LocalDate.of(2025, 6, 18);
    public static LocalTime time = LocalTime.of(11, 5);

    @AfterEach
    public void afterEach() {
        if (filePath.exists()) {
            filePath.delete();
        }
    }
}
