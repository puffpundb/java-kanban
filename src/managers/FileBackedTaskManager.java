package managers;

import exceptions.ManagerSaveException;
import tasks.Epic;
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

    private void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(fileToSave)) {
            writer.write("id,type,name,status,description,(epic)");
            writer.newLine();

            for (Task task : getAllTasks()) {
                writer.write(Converter.taskToString(task));
                writer.newLine();

            }

            for (Task epic : getAllEpics()) {
                writer.write(Converter.taskToString(epic));
                writer.newLine();

            }

            for (Task sub : getAllSubTasks()) {
                writer.write(Converter.taskToString(sub));
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

                if (taskLine.contains("id,type,name,status,description,(epic)")) continue;

                Task currentTask = Converter.taskFromString(taskLine);
                if (currentTask.getId() > maxId) maxId = currentTask.getId();
                manager.putTaskToSave(currentTask);

            }

            return manager;

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки файла");
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

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTask(Integer id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubTask(Integer id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteEpic(Integer id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public void clearAllSubTasks() {
        super.clearAllSubTasks();
        save();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        save();
    }

    public void putTaskToSave(Task task) {
        switch (task.getType()) {
            case TASK -> updateTask(task);
            case EPIC -> updateEpic((Epic) task);
            case SUBTASK -> updateSubTask((SubTask) task);

        }
    }
}
