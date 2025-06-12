package managers;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path fileToSave;

    public FileBackedTaskManager(Path fileToSave) {
        this.fileToSave = fileToSave;
    }

    public Path getFileToSave() {
        return fileToSave;
    }

    public void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(fileToSave)) {
            for (Task task : getAllTasks()) {
                writer.write(taskToString(task));
                writer.newLine();

            }

            for (Task epic : getAllEpics()) {
                writer.write(taskToString(epic));
                writer.newLine();

            }

            for (Task sub : getAllSubTasks()) {
                writer.write(taskToString(sub));
                writer.newLine();

            }
        } catch (IOException e) {
            throw new ManagerSaveException("Неизвестный тип задачи");
        }
    }

    public static FileBackedTaskManager loadFromFile(Path file) {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            FileBackedTaskManager manager = new FileBackedTaskManager(file);
            String taskLine;
            int maxId = 0;

            while (reader.ready()) {
                taskLine = reader.readLine();
                Task currentTask = manager.taskFromString(taskLine);
                manager.putTaskToSave(currentTask);

            }

            return manager;

        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки файла" + e.getMessage());
        }

    }

    @Override
    public void createNewTask(Task newTask) {
        super.createNewTask(newTask);
        save();
    }

    @Override
    public void createNewSubTask(SubTask newSubTask) {
        super.createNewSubTask(newSubTask);
        save();
    }

    @Override
    public void createNewEpic(Epic newEpic) {
        super.createNewEpic(newEpic);
        save();
    }

    private String taskToString(Task task) {
        switch (task.getType()) {
            case TASK -> {
                return String.format("%d,%s,%s,%s,%s",
                        task.getId(),
                        task.getType(),
                        task.getTitle(),
                        task.getStatus(),
                        task.getDescription());

            }
            case EPIC -> {
                Epic epic = (Epic) task;

                return String.format("%d,%s,%s,%s,%s",
                        epic.getId(),
                        epic.getType(),
                        epic.getTitle(),
                        epic.getStatus(),
                        epic.getDescription());

            }
            case SUBTASK -> {
                SubTask sub = (SubTask) task;

                return String.format("%d,%s,%s,%s,%s,%d",
                        sub.getId(),
                        sub.getType(),
                        sub.getTitle(),
                        sub.getStatus(),
                        sub.getDescription(),
                        sub.getEpicId());

            }
            default -> {
                return "";
            }
        }
    }

    public Task taskFromString(String value) {
        String[] stringToTask = value.split(",");

        switch (stringToTask[1]) {
            case "TASK" -> {
                Task newTask = new Task(stringToTask[2], stringToTask[4]);
                newTask.setId(Integer.valueOf(stringToTask[0]));
                newTask.setStatus(Status.valueOf(stringToTask[3]));

                return newTask;

            }
            case "EPIC" -> {
                Epic newEpic = new Epic(stringToTask[2], stringToTask[4]);
                newEpic.setId(Integer.valueOf(stringToTask[0]));
                newEpic.setStatus(Status.valueOf(stringToTask[3]));

                return newEpic;

            }
            case "SUBTASK" -> {
                SubTask newSubTask = new SubTask(stringToTask[2], stringToTask[4], Integer.valueOf(stringToTask[5]));
                newSubTask.setId(Integer.valueOf(stringToTask[0]));
                newSubTask.setStatus(Status.valueOf(stringToTask[3]));

                return newSubTask;

            }
            default -> {
                return null;

            }
        }
    }
}
