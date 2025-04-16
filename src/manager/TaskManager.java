package manager;

import tasks.Epic;
import tasks.Task;
import tasks.SubTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    private static int globalId = 0;
    private final HashMap<Integer, Task> savedTasks = new HashMap<>();
    private final HashMap<Integer, SubTask> savedSubTasks = new HashMap<>();
    private final HashMap<Integer, Epic> savedEpics = new HashMap<>();

    private static Integer generateId() {
        return globalId++;
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(savedTasks.values());
    }

    public ArrayList<Task> getAllSubTasks() {
        return new ArrayList<>(savedSubTasks.values());
    }

    public ArrayList<Task> getAllEpics() {
        return new ArrayList<>(savedEpics.values());
    }

    public void clearAllTasks() {
        savedTasks.clear();
        savedSubTasks.clear();
        savedEpics.clear();
    }

    public Epic getEpicById(Integer id) {
        return savedEpics.get(id);
    }

    public Task getTaskById(Integer id) {
        return savedTasks.get(id);
    }

    public SubTask getSubTaskById(Integer id) {
        return savedSubTasks.get(id);
    }

    public void createNewTask(String title, String description) {
        Task newTask = new Task(title, description, generateId());

        savedTasks.put(newTask.getId(), newTask);
    }

    public void createNewSubTask(String title, String description, Integer epicId) {
        SubTask newSub = new SubTask(title, description, generateId(), epicId);

        savedSubTasks.put(newSub.getId(), newSub);
        savedEpics.get(epicId).putSubsId(newSub.getId());
        updateEpic(epicId);
    }

    public void createNewEpic(String title, String description) {
        Epic newEpic = new Epic(title, description, generateId());

        savedEpics.put(newEpic.getId(), newEpic);
    }

    public void updateTask(Integer taskId) {
        Task currentTask = savedTasks.get(taskId);

        if (currentTask.getStatus() == Status.NEW) {
            currentTask.setStatus(Status.DONE);
        } else {
            currentTask.setStatus(Status.NEW);
        }
    }

    public void updateSubTask(Integer subTaskId) {
        SubTask currentSubTask = savedSubTasks.get(subTaskId);

        if (currentSubTask.getStatus() == Status.NEW) {
            currentSubTask.setStatus(Status.DONE);
        } else {
            currentSubTask.setStatus(Status.NEW);
        }

        updateEpic(currentSubTask.getEpicId());
    }

    public void updateEpic(Integer epicId) {
        Epic epic = savedEpics.get(epicId);

        if (epic.getSubsId().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        int doneCounter = 0;
        int newCounter = 0;
        int allSubsCount = epic.getSubsId().size();

        for (Integer subId : epic.getSubsId()) {
            SubTask subTask = getSubTaskById(subId);
            if (subTask.getStatus() == Status.DONE) {
                doneCounter++;
            } else {
                newCounter++;
            }
        }

        if (doneCounter == allSubsCount) {
            epic.setStatus(Status.DONE);
        } else if (newCounter == allSubsCount) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }

        savedEpics.put(epic.getId(), epic);
    }

    public void deleteTask(Integer id) {
        savedTasks.remove(id);
    }

    public void deleteSubTask(Integer id) {
        SubTask currentSubTask = savedSubTasks.get(id);
        Epic currentEpic = savedEpics.get(currentSubTask.getEpicId());
        int toDelete = 0;

        for (Integer epicSub : currentEpic.getSubsId()) {
            if (Objects.equals(epicSub, id)) {
                currentEpic.deleteSub(toDelete);
                break;
            }

            toDelete++;
        }

        updateEpic(currentSubTask.getEpicId());

        savedSubTasks.remove(id);
    }

    public void deleteEpic(Integer id) {
        Epic currentEpic = savedEpics.get(id);

        for (Integer currentEpicSubsId : currentEpic.getSubsId()) {
            savedSubTasks.remove(currentEpicSubsId);
        }

        savedEpics.remove(id);
    }

    public ArrayList<SubTask> getEpicSubs(Integer id) {
        Epic epic = savedEpics.get(id);
        ArrayList<SubTask> taskList = new ArrayList<>();

        for (Integer subId : epic.getSubsId()) {
            taskList.add(savedSubTasks.get(subId));
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