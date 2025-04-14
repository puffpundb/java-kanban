package manager;

import tasks.Epic;
import tasks.Task;
import tasks.SubTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    private static int globalId = 0;
    private final HashMap<Integer, Epic> savedTasks = new HashMap<>();

    public static Integer generateId() {
        return globalId++;
    }

    public ArrayList<Epic> getAllTasks() {
        return new ArrayList<>(savedTasks.values());
    }

    public void clearTasks() {
        savedTasks.clear();
    }

    public Epic getById(Integer id) {
        return savedTasks.get(id);
    }

    public void createNewTask(Epic task) {
        savedTasks.put(task.getId(), task);
    }

    public void createNewTask(Task task) {
        savedTasks.put(task.getId(), task);
    }

    public void createNewTask(SubTask subTask, Integer epicId) {
        Epic currentEpic = savedTasks.get(epicId);
        currentEpic.putSubs(subTask);
        savedTasks.put(subTask.getId(), subTask);
    }

    public void updateTask(Integer id) {
        if (savedTasks.get(id).getClass() == Task.class) {
            Task currentTask = (Task)savedTasks.get(id);

            if (currentTask.getStatus() == Status.NEW) {
                currentTask.setStatus(Status.DONE);
            } else {
                currentTask.setStatus(Status.NEW);
            }

        } else if (savedTasks.get(id).getClass() == SubTask.class) {
            SubTask currentSubTask = (SubTask)savedTasks.get(id);

            if (currentSubTask.getStatus() == Status.NEW) {
                currentSubTask.setStatus(Status.DONE);
            } else {
                currentSubTask.setStatus(Status.NEW);
            }

            updateTask(currentSubTask.getEpicId());

        } else if (savedTasks.get(id).getClass() == Epic.class) {
            Epic task = savedTasks.get(id);

            if (task.getSubsId().isEmpty()) {
                task.setStatus(Status.NEW);
                return;
            }

            int doneCounter = 0;
            int newCounter = 0;
            int allSubsCount = task.getSubsId().size();

            for (Integer subId : task.getSubsId()) {
                SubTask subTask = (SubTask)getById(subId);
                if (subTask.getStatus() == Status.DONE) {
                    doneCounter++;
                } else {
                    newCounter++;
                }
            }

            if (doneCounter == allSubsCount) {
                task.setStatus(Status.DONE);
            } else if (newCounter == allSubsCount) {
                task.setStatus(Status.NEW);
            } else {
                task.setStatus(Status.IN_PROGRESS);
            }

            savedTasks.put(task.getId(), task);
        }
    }

    public void deleteTask(Integer id) {
        if (savedTasks.get(id).getClass() == Task.class) {
            savedTasks.remove(id);

        } else if (savedTasks.get(id).getClass() == SubTask.class) {
            SubTask currentSubTask = (SubTask)savedTasks.get(id);
            Epic currentEpic = savedTasks.get(currentSubTask.getEpicId());
            int toDelete = 0;

            for (Integer epicSub : currentEpic.getSubsId()) {
                if (Objects.equals(epicSub, id)) {
                    currentEpic.deleteSub(toDelete);
                    break;
                }
            }
            updateTask(currentSubTask.getEpicId());

            savedTasks.remove(id);

        } else {
            Epic currentEpic = savedTasks.get(id);
            savedTasks.remove(id);

            for (Integer currentEpicSubsId : currentEpic.getSubsId()) {
                savedTasks.remove(currentEpicSubsId);
            }
        }
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> currentEpics = new ArrayList<>();

        for (Epic task : savedTasks.values()) {
            if (task.getClass() == Epic.class) {
                currentEpics.add(task);
            }
        }

        return currentEpics;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> currentTask = new ArrayList<>();

        for (Epic task : savedTasks.values()) {
            if (task.getClass() == Task.class) {
                currentTask.add((Task)task);
            }
        }

        return currentTask;
    }

    public ArrayList<SubTask> getEpicSubs(Integer id) {
        Epic epic = savedTasks.get(id);
        ArrayList<SubTask> taskList = new ArrayList<>();

        for (Integer subId : epic.getSubsId()) {
            SubTask subTask = (SubTask)savedTasks.get(subId);
            taskList.add(subTask);
        }

        return taskList;
    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "savedTasks=" + savedTasks +
                '}';
    }
}