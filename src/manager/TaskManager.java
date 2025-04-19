package manager;

import tasks.Epic;
import tasks.Task;
import tasks.SubTask;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int globalId = 0;
    private final HashMap<Integer, Task> savedTasks = new HashMap<>();
    private final HashMap<Integer, SubTask> savedSubTasks = new HashMap<>();
    private final HashMap<Integer, Epic> savedEpics = new HashMap<>();

    private Integer generateId() {
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
    }

    public void clearAllSubTasks() {
        savedSubTasks.clear();
    }

    public void clearAllEpics() {
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

    public void createNewTask(Task newTask) {
        newTask.setId(generateId());
        savedTasks.put(newTask.getId(), newTask);
    }

    public void createNewSubTask(SubTask newSubTask) {
        newSubTask.setId(generateId());

        if (savedEpics.containsKey(newSubTask.getEpicId())) {
            savedSubTasks.put(newSubTask.getId(), newSubTask);
            savedEpics.get(newSubTask.getEpicId()).putSubsId(newSubTask.getId());
            updateEpicStatus(newSubTask.getEpicId());
        }
    }

    public void createNewEpic(Epic newEpic) {
        newEpic.setId(generateId());

        savedEpics.put(newEpic.getId(), newEpic);
    }

    public void updateTask(Task task) {
        savedTasks.put(task.getId(), task);
    }

    public void updateSubTask(SubTask subTask) {
        savedSubTasks.put(subTask.getId(), subTask);

        updateEpicStatus(subTask.getEpicId());
    }

    public void updateEpic(Epic epic) {
        savedEpics.put(epic.getId(), epic);

        updateEpicStatus(epic.getId());
    }

    private void updateEpicStatus(Integer id) {
        Epic epic = savedEpics.get(id);

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

            } else if (subTask.getStatus() == Status.NEW) {
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
    }

    public void deleteTask(Integer id) {
        savedTasks.remove(id);
    }

    public void deleteSubTask(Integer id) {
        SubTask currentSubTask = savedSubTasks.get(id);
        Epic currentEpic = savedEpics.get(currentSubTask.getEpicId());

        currentEpic.deleteSub(id);
        updateEpicStatus(currentSubTask.getEpicId());

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
}