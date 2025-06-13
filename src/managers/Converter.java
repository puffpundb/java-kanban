package managers;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.Arrays;

public class Converter {
    public static String taskToString(Task task) {
        switch (task.getType()) {
            case TASK, EPIC -> {
                return String.format("%d,%s,%s,%s,%s",
                        task.getId(),
                        task.getType(),
                        task.getTitle(),
                        task.getStatus(),
                        task.getDescription());

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
            default -> throw new ManagerSaveException("Неизвестный тип задачи" + task.getType());
        }
    }

    public static Task taskFromString(String value) {
        String[] stringToTask = value.split(",");

        if (stringToTask.length < 5) {
            throw new ManagerSaveException("Некорректная строка" + Arrays.toString(stringToTask));
        }

        try {
            Types type = Types.valueOf(stringToTask[1]);

            switch (type) {
                case TASK, EPIC -> {
                    Task newTask;

                    if (type.equals(Types.TASK)) newTask = new Task(stringToTask[2], stringToTask[4]);
                    else newTask = new Epic(stringToTask[2], stringToTask[4]);

                    newTask.setId(Integer.valueOf(stringToTask[0]));
                    newTask.setStatus(Status.valueOf(stringToTask[3]));

                    return newTask;

                }
                case SUBTASK -> {
                    SubTask newTask = new SubTask(stringToTask[2], stringToTask[4], Integer.valueOf(stringToTask[5]));
                    newTask.setId(Integer.valueOf(stringToTask[0]));
                    newTask.setStatus(Status.valueOf(stringToTask[3]));

                    return newTask;

                }
                default -> throw new ManagerSaveException("Неизвестный тип задачи" + stringToTask[1]);

            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при чтении задачи: " + value, e);
        }
    }
}
